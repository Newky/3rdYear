#include "SymbolTable.h"
#include "Parser.h"

namespace Taste {

SymbolTable::SymbolTable(Parser *parser): undef(0), integer(1), boolean(2), var(0), proc(1), scope(2) {
	errors = parser->errors;
	topScope = NULL;
	curLevel = -1;
	undefObj = new Obj();
	undefObj->name  = coco_string_create("undef"); undefObj->type = undef; undefObj->kind = var;
	undefObj->adr = 0; undefObj->level = 0; undefObj->next = NULL;
}

void SymbolTable::Err(wchar_t* msg) {
	errors->Error(0, 0, msg);
}


// open a new scope and make it the current scope (topScope)
void SymbolTable::OpenScope () {
	Obj *scop = new Obj();
	scop->name = coco_string_create(""); scop->kind = scope;
	scop->locals = NULL; scop->nextAdr = 0;
	scop->next = topScope;
	//This is the case as nothing is defined in the scope as its 
	// a new scope
	topScope = scop;
	curLevel++;
}


// close the current scope
void SymbolTable::CloseScope () {
	topScope = topScope->next; curLevel--;
}

// create a new object node in the current scope
Obj* SymbolTable::NewObj (wchar_t* name, int kind, int type,int constant,  int size) {
	int i=0;
	Obj *firstobj;
	char *name_str;
	name_str = coco_string_create_char(name);
	//name_str contains variable name + index + ?
	//i.e arr50?
	//I know this is hacky but it works quite well
	if(name_str[strlen(name_str)-1] == '?'){
		//arr size is to hold an int representation of the array size
		int arr_size= 0,j=0;
		int length = strlen(name_str);
		i = length-2;
		/*
		 *This while loop just generates the number from the end of the string into an int
		 *It then truncates the string to remove the numbers to be used for the next section
		 * I do this by just using the string terminating symbol.
		 */
		while(name_str[i] >= '0' && name_str[i] <= '9'){
			if(j != 0)
				arr_size += (name_str[i] - 48) * (10*j++);
			else{
				arr_size = name_str[i] - 48;j++;
			}
			i--;
		}
		i++;
		name_str[i++] = '\0';
		size = arr_size;
	}
	/*Satisfy coco/r's mad wchar_t
	 */
	name = coco_string_create(name_str);
	for (i = 0; i < size; i++) {
		Obj *p, *last, *obj = new Obj();
		char number[strlen(coco_string_create_char(name))+1];
		/*
		 *If this is an array then I make the name a combination of the name of the array the index and a ? to show its an array
		 */
		if(size == 1)
			sprintf(number, "%s", coco_string_create_char(name));
		else
			sprintf(number, "%s%d?", coco_string_create_char(name), i);
		obj->name = coco_string_create(number); obj->kind = kind; obj->type = type;
		obj->level = curLevel;
		obj->constant = constant;
		p = topScope->locals; last = NULL;
		while (p != NULL) {
			if (coco_string_equal(p->name, name)) Err(L"name declared twice");
			last = p; p = p->next;
		}
		//If first object in scope
		if (last == NULL) topScope->locals = obj; else last->next = obj;
		if (kind == var) obj->adr = topScope->nextAdr++;
		if(i == 0)
			firstobj = obj;
	}
	return firstobj;
}

// search the name in all open scopes and return its object node
Obj* SymbolTable::Find (wchar_t* name, int index) {
	Obj *obj, *scope;
	scope = topScope;
	char *name_str;
	/*
	 *This is just handling out of bounds array exceptions instead of giving a 
	 Ambigious error, it will return out of bounds because of the array_var toggle.
	 */
	name_str = coco_string_create_char(name);
	int length = strlen(name_str), array_var;
	if(name_str[length-1] == '?'){
		array_var = 1;
	}
	//name = coco_string_create(name_str);
	while (scope != NULL) {  // for all open scopes
		obj = scope->locals;
		while (obj != NULL) {  // for all objects in this scope
			if (coco_string_equal(obj->name, name)) return obj;
			obj = obj->next;
		}
		scope = scope->next;
	}
	wchar_t str[100];
	if(array_var)
	coco_swprintf(str, 100, L"Array index out of bounds", name);
	else
	coco_swprintf(str, 100, L"%ls is undeclared", name);
	Err(str);
	return undefObj;
}

}; // namespace
