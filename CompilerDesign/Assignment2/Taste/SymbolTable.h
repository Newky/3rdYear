#if !defined(TASTE_SYMBOLTABLE_H__)
#define TASTE_SYMBOLTABLE_H__

#include "Scanner.h"

namespace Taste {

class Parser;
class Errors;

class Obj {  // object describing a declared name
public:
	wchar_t* name;		// name of the object
	int type;		// type of the object (undef for proc)
	Obj	*next;		// to next object in same scope
	int kind;		// var, proc, scope
	int adr;		// address in memory or start of proc
	int level;		// nesting level; 0=global, 1=local
	Obj *locals;		// scopes: to locally declared objects
	int nextAdr;		// scopes: next free address in this scope
	int constant;		// Toggles constant or not.. quite hacky 0=not const, 1=const_unset, 2=const_set;

	Obj() {
		name    = NULL;
		type    = 0;
		next    = NULL;
		kind    = 0;
		adr     = 0;
		level   = 0;
		locals  = NULL;
		nextAdr = 0;
		constant = 0;
	}

	~Obj() {
		coco_string_delete(name);
	}
};

class SymbolTable
{
public:
	const int // types
		undef, integer, boolean;

	const int // object kinds
		var, proc, scope;


	int curLevel;	// nesting level of current scope
	Obj *undefObj;	// object node for erroneous symbols
	Obj *topScope;	// topmost procedure scope

	Errors *errors;

	SymbolTable(Parser *parser);
	void Err(wchar_t* msg);

	// open a new scope and make it the current scope (topScope)
	void OpenScope ();

	// close the current scope
	void CloseScope ();

	// create a new object node in the current scope
	Obj* NewObj (wchar_t* name, int kind, int type,int constant=0, int size=1); 

	// search the name in all open scopes and return its object node
	Obj* Find (wchar_t* name, int index=1);

};

}; // namespace

#endif // !defined(TASTE_SYMBOLTABLE_H__)
