

#include <wchar.h>
#include "Parser.h"
#include "Scanner.h"


namespace Taste {


void Parser::SynErr(int n) {
	if (errDist >= minErrDist) errors->SynErr(la->line, la->col, n);
	errDist = 0;
}

void Parser::SemErr(const wchar_t* msg) {
	if (errDist >= minErrDist) errors->Error(t->line, t->col, msg);
	errDist = 0;
}

void Parser::Get() {
	for (;;) {
		t = la;
		la = scanner->Scan();
		if (la->kind <= maxT) { ++errDist; break; }

		if (dummyToken != t) {
			dummyToken->kind = t->kind;
			dummyToken->pos = t->pos;
			dummyToken->col = t->col;
			dummyToken->line = t->line;
			dummyToken->next = NULL;
			coco_string_delete(dummyToken->val);
			dummyToken->val = coco_string_create(t->val);
			t = dummyToken;
		}
		la = t;
	}
}

void Parser::Expect(int n) {
	if (la->kind==n) Get(); else { SynErr(n); }
}

void Parser::ExpectWeak(int n, int follow) {
	if (la->kind == n) Get();
	else {
		SynErr(n);
		while (!StartOf(follow)) Get();
	}
}

bool Parser::WeakSeparator(int n, int syFol, int repFol) {
	if (la->kind == n) {Get(); return true;}
	else if (StartOf(repFol)) {return false;}
	else {
		SynErr(n);
		while (!(StartOf(syFol) || StartOf(repFol) || StartOf(0))) {
			Get();
		}
		return StartOf(syFol);
	}
}

void Parser::AddOp(int &op) {
		op = -1; 
		if (la->kind == 3) {
			Get();
			op = plus; 
		} else if (la->kind == 4) {
			Get();
			op = minus; 
		} else SynErr(47);
}

void Parser::Expr(int &type) {
		int type1, op, adr;
		SimExpr(type);
		if (StartOf(1)) {
			RelOp(op);
			SimExpr(type1);
			if (type != type1) Err(L"incompatible types");
			gen->Emit(op); type = boolean; 
		}
}

void Parser::SimExpr(int &type) {
		int type1, op; 
		Term(type);
		while (la->kind == 3 || la->kind == 4) {
			AddOp(op);
			Term(type1);
			if ((type != integer && type != character) || (type1 != integer && type != character)) 
			     Err(L"integer type expected SimExpr");
			   gen->Emit(op); 
		}
}

void Parser::RelOp(int &op) {
		op = -1; 
		switch (la->kind) {
		case 17: {
			Get();
			op = equ; 
			break;
		}
		case 18: {
			Get();
			op = lss; 
			break;
		}
		case 19: {
			Get();
			op = gtr; 
			break;
		}
		case 20: {
			Get();
			op = ne;  
			break;
		}
		case 21: {
			Get();
			op = leq;  
			break;
		}
		case 22: {
			Get();
			op = geq;  
			break;
		}
		default: SynErr(48); break;
		}
}

void Parser::Factor(int &type) {
		int n; Obj *obj; wchar_t* name; 
		type = undef; 
		switch (la->kind) {
		case 1: {
			Ident(name);
			obj = tab->Find(name); type = obj->type;
			                  if (obj->kind == var) {
								if (obj->level == 0) gen->Emit(LOADG, obj->adr);
								else gen->Emit(LOAD, obj->adr);
			                  } else Err(L"variable expected"); 
			break;
		}
		case 2: {
			Get();
			swscanf(t->val, L"%d", &n);	//n = Convert.ToInt32(t->val); 
			gen->Emit(CONST, n); type = integer; 
			break;
		}
		case 4: {
			Get();
			Factor(type);
			if (type != integer) {
			 Err(L"integer type expected Factor"); type = integer;
			}
			gen->Emit(NEG); 
			break;
		}
		case 5: {
			Get();
			gen->Emit(CONST, 1); type = boolean; 
			break;
		}
		case 6: {
			Get();
			gen->Emit(CONST, 0); type = boolean; 
			break;
		}
		case 7: {
			Get();
			Expect(1);
			swscanf(t->val, L"%c", &n);	
			             gen->Emit(CONST, n); type = character; 
			Expect(7);
			
			break;
		}
		default: SynErr(49); break;
		}
}

void Parser::Ident(wchar_t* &name) {
		int n; 
		Expect(1);
		name = coco_string_create(t->val); 
		while (la->kind == 8) {
			Get();
			Expect(2);
			swscanf(t->val, L"%d", &n);
			int copy = n;
			int width = 1;
			copy = copy / 10;
			while(copy > 0) {width++;copy=copy/10;}
			char number[strlen(coco_string_create_char(name))+width+1];
			sprintf(number, "%s%d?", coco_string_create_char(name), n);
			name = coco_string_create(number);
					
			Expect(9);
			
		}
}

void Parser::MulOp(int &op) {
		op = -1; 
		if (la->kind == 10) {
			Get();
			op = times; 
		} else if (la->kind == 11) {
			Get();
			op = slash; 
		} else SynErr(50);
}

void Parser::ProcDecl() {
		wchar_t* name; Obj *obj; int adr; 
		Expect(12);
		Ident(name);
		obj = tab->NewObj(name, proc, undef, 0); obj->adr = gen->pc;
		if (coco_string_equal(name, L"Main")) gen->progStart = gen->pc; 
		tab->OpenScope(); 
		Expect(13);
		Expect(14);
		Expect(15);
		gen->Emit(ENTER, 0); adr = gen->pc - 2; 
		while (StartOf(2)) {
			if (StartOf(3)) {
				VarDecl();
			} else {
				Stat();
			}
		}
		Expect(16);
		gen->Emit(LEAVE); gen->Emit(RET);
		gen->Patch(adr, tab->topScope->nextAdr);
		tab->CloseScope(); 
}

void Parser::VarDecl() {
		wchar_t* name; int type,constant=0;
		while (la->kind == 44) {
			Get();
			constant = 1;
		}
		Type(type);
		Ident(name);
		tab->NewObj(name, var, type, constant); 
		
		while (la->kind == 45) {
			Get();
			Ident(name);
			tab->NewObj(name, var, type, constant); 
		}
		Expect(24);
}

void Parser::Stat() {
		int type, type2, type3,n; wchar_t* name; Obj *obj; int adr, adr2, loopstart, loopstart2; 
		switch (la->kind) {
		case 1: {
			Ident(name);
			obj = tab->Find(name); 
			if (la->kind == 23) {
				Get();
				if (StartOf(4)) {
					if (obj->kind != var) Err(L"cannot assign to procedure");
					if(obj->constant >= 2) Err(L"cannot assign to constant");
					if(obj->constant == 1) obj->constant ++;
					
					Expr(type);
					Expect(24);
					if (type != obj->type) Err(L"incompatible types");
					if (obj->level == 0) gen->Emit(STOG, obj->adr);
					else gen->Emit(STO, obj->adr); 
				} else if (la->kind == 13) {
					Get();
					Expr(type);
					if (type != boolean) Err(L"boolean type expected richy ");
					gen->Emit(FJMP, 0); adr = gen->pc -2;
					Expect(14);
					
					Expect(25);
					Expr(type2);
					if (type2 != obj->type) Err(L"incompatible types richy");	 
					if (obj->level == 0) gen->Emit(STOG, obj->adr);
					else gen->Emit(STO, obj->adr); 
					gen->Emit(JMP, 0); adr2 = gen->pc - 2; gen->Patch(adr, gen->pc); adr = adr2;
					
					Expect(26);
					Expr(type3);
					gen->Patch(adr, gen->pc); if(type3 != obj->type) Err(L"incompatible types richy");	 
					if (obj->level == 0) gen->Emit(STOG, obj->adr);
					else gen->Emit(STO, obj->adr); 
					
					Expect(24);
					gen->Patch(adr, gen->pc); 
				} else SynErr(51);
			} else if (la->kind == 27 || la->kind == 28) {
				if (obj->kind != var) Err(L"cannot assign to procedure");
					if(obj->constant > 1) Err(L"cannot increment a constant");
					gen->Emit(CONST, 1);
					gen->Emit(LOAD, obj->adr);
					 
				
				if (la->kind == 27) {
					Get();
					gen->Emit(ADD);
				} else {
					Get();
					gen->Emit(SUB);
					
					
				}
				gen->Emit(STO, obj->adr);
				Expect(24);
			} else if (la->kind == 13) {
				Get();
				Expect(14);
				if (obj->kind != proc) Err(L"object is not a procedure");
				gen->Emit(CALL, obj->adr); 
				Expect(24);
			} else SynErr(52);
			break;
		}
		case 29: {
			Get();
			Expect(13);
			Expr(type);
			Expect(14);
			if (type != boolean) Err(L"boolean type expected");
			gen->Emit(FJMP, 0); adr = gen->pc - 2; 
			Stat();
			if (la->kind == 30) {
				Get();
				gen->Emit(JMP, 0); adr2 = gen->pc - 2;
				gen->Patch(adr, gen->pc);
				adr = adr2; 
				Stat();
			}
			gen->Patch(adr, gen->pc); 
			break;
		}
		case 31: {
			Get();
			Expect(13);
			Expr(type);
			Expect(14);
			if (type != boolean) Err(L"boolean type expected");
			gen->Emit(FJMP, 0); adr = gen->pc - 2; 
			gen->Emit(JMP, 0); adr2 = gen->pc -2;	
			gen->Patch(adr, gen->pc); 
			Stat();
			gen->Patch(adr2, gen->pc); 
			break;
		}
		case 32: {
			Get();
			loopstart = gen->pc; 
			Expect(13);
			Expr(type);
			Expect(14);
			if (type != boolean) Err(L"boolean type expected");
			gen->Emit(FJMP, 0); adr = gen->pc - 2; 
			Stat();
			gen->Emit(JMP, loopstart); gen->Patch(adr, gen->pc); 
			break;
		}
		case 33: {
			Get();
			Expect(13);
			Stat();
			loopstart = gen->pc;
			Expr(type);
			if (type != boolean) Err(L"boolean type expected");
			gen->Emit(FJMP, 0); adr= gen->pc -2; 
			Expect(24);
			gen->Emit(JMP, 0); adr2 = gen->pc - 2;loopstart2 = gen->pc;
			Stat();
			gen->Emit(JMP, loopstart); 
			Expect(14);
			gen->Patch(adr2, gen->pc); 
			Stat();
			gen->Emit(JMP, loopstart2); gen->Patch(adr, gen->pc);	
			
			break;
		}
		case 34: {
			Get();
			Ident(name);
			Expect(24);
			obj = tab->Find(name);
			if (obj->type != integer) Err(L"integer type expected");
			gen->Emit(READ);
			if (obj->level == 0) gen->Emit(STOG, obj->adr);
			else gen->Emit(STO, obj->adr); 
			break;
		}
		case 35: {
			Get();
			Expect(13);
			Ident(name);
			obj = tab->Find(name);int t=0;int adr2;
			if(obj->type != integer) Err(L"integer type expected in write");
			if(obj->level == 0) {gen->Emit(LOADG, obj->adr);;}
			else {gen->Emit(LOAD, obj->adr);}
			
			Expect(14);
			Expect(15);
			Expect(36);
			Expr(type);
			if(type != obj->type) Err( L" incompatible types in case" ); 
			gen->Emit(EQU); 
			gen->Emit(FJMP,0); 
			adr = gen->pc - 2;
			t=1;
			
			Expect(26);
			Stat();
			Expect(37);
			loopstart=gen->pc;gen->Emit(JMP, 0);adr2=gen->pc-2;
			Expect(24);
			while (la->kind == 36) {
				Get();
				gen->Patch(adr, gen->pc); if(obj->level == 0) {gen->Emit(LOADG, obj->adr);;}
				else {gen->Emit(LOAD, obj->adr);
				}
				 
				Expr(type);
				if(type != obj->type) Err( L" incompatible types in case" ); 
				gen->Emit(EQU); 
				gen->Emit(FJMP,0); 
				adr = gen->pc - 2;
				t=1;
				
				Expect(26);
				Stat();
				Expect(37);
				gen->Emit(JMP, loopstart);
				Expect(24);
			}
			while (la->kind == 38) {
				Get();
				if(!t){gen->Patch(adr, gen->pc);t=1;}else{t=0;}
				Stat();
				Expect(37);
				Expect(24);
			}
			Expect(16);
			gen->Patch(adr2, gen->pc); 
			break;
		}
		case 39: {
			Get();
			Expr(type);
			Expect(24);
			if (type == integer) gen->Emit(WRITE);  
			else if(type == character) gen->Emit(WRITEC);
			else Err(L"integer type expected in write");
										   
			
			break;
		}
		case 15: {
			Get();
			while (StartOf(2)) {
				if (StartOf(5)) {
					Stat();
				} else {
					VarDecl();
				}
			}
			Expect(16);
			break;
		}
		default: SynErr(53); break;
		}
}

void Parser::Term(int &type) {
		int type1, op; 
		Factor(type);
		while (la->kind == 10 || la->kind == 11) {
			MulOp(op);
			Factor(type1);
			if ((type == integer || type == character) && (type1 == integer || type1 == character)) 
			gen->Emit(op); 
			  Err(L"integer type expected in Term");
		}
}

void Parser::Taste() {
		wchar_t* name;
		InitDeclarations(); 
		Expect(40);
		Ident(name);
		tab->OpenScope(); 
		Expect(15);
		while (StartOf(3)) {
			VarDecl();
		}
		while (la->kind == 12) {
			ProcDecl();
		}
		Expect(16);
		tab->CloseScope(); 
}

void Parser::Type(int &type) {
		type = undef; 
		if (la->kind == 41) {
			Get();
			type = integer; 
		} else if (la->kind == 42) {
			Get();
			type = boolean; 
		} else if (la->kind == 43) {
			Get();
			type = character; 
		} else SynErr(54);
}



void Parser::Parse() {
	t = NULL;
	la = dummyToken = new Token();
	la->val = coco_string_create(L"Dummy Token");
	Get();
	Taste();

}

Parser::Parser(Scanner *scanner) {
	maxT = 46;

	dummyToken = NULL;
	t = la = NULL;
	minErrDist = 2;
	errDist = minErrDist;
	this->scanner = scanner;
	errors = new Errors();
}

bool Parser::StartOf(int s) {
	const bool T = true;
	const bool x = false;

	static bool set[6][48] = {
		{T,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x},
		{x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,T,T,T, T,T,T,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x},
		{x,T,x,x, x,x,x,x, x,x,x,x, x,x,x,T, x,x,x,x, x,x,x,x, x,x,x,x, x,T,x,T, T,T,T,T, x,x,x,T, x,T,T,T, T,x,x,x},
		{x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,T,T,T, T,x,x,x},
		{x,T,T,x, T,T,T,T, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x, x,x,x,x},
		{x,T,x,x, x,x,x,x, x,x,x,x, x,x,x,T, x,x,x,x, x,x,x,x, x,x,x,x, x,T,x,T, T,T,T,T, x,x,x,T, x,x,x,x, x,x,x,x}
	};



	return set[s][la->kind];
}

Parser::~Parser() {
	delete errors;
	delete dummyToken;
}

Errors::Errors() {
	count = 0;
}

void Errors::SynErr(int line, int col, int n) {
	wchar_t* s;
	switch (n) {
			case 0: s = coco_string_create(L"EOF expected"); break;
			case 1: s = coco_string_create(L"ident expected"); break;
			case 2: s = coco_string_create(L"number expected"); break;
			case 3: s = coco_string_create(L"\"+\" expected"); break;
			case 4: s = coco_string_create(L"\"-\" expected"); break;
			case 5: s = coco_string_create(L"\"true\" expected"); break;
			case 6: s = coco_string_create(L"\"false\" expected"); break;
			case 7: s = coco_string_create(L"\"\'\" expected"); break;
			case 8: s = coco_string_create(L"\"[\" expected"); break;
			case 9: s = coco_string_create(L"\"]\" expected"); break;
			case 10: s = coco_string_create(L"\"*\" expected"); break;
			case 11: s = coco_string_create(L"\"/\" expected"); break;
			case 12: s = coco_string_create(L"\"void\" expected"); break;
			case 13: s = coco_string_create(L"\"(\" expected"); break;
			case 14: s = coco_string_create(L"\")\" expected"); break;
			case 15: s = coco_string_create(L"\"{\" expected"); break;
			case 16: s = coco_string_create(L"\"}\" expected"); break;
			case 17: s = coco_string_create(L"\"==\" expected"); break;
			case 18: s = coco_string_create(L"\"<\" expected"); break;
			case 19: s = coco_string_create(L"\">\" expected"); break;
			case 20: s = coco_string_create(L"\"!=\" expected"); break;
			case 21: s = coco_string_create(L"\"<=\" expected"); break;
			case 22: s = coco_string_create(L"\">=\" expected"); break;
			case 23: s = coco_string_create(L"\":=\" expected"); break;
			case 24: s = coco_string_create(L"\";\" expected"); break;
			case 25: s = coco_string_create(L"\"?\" expected"); break;
			case 26: s = coco_string_create(L"\":\" expected"); break;
			case 27: s = coco_string_create(L"\"++\" expected"); break;
			case 28: s = coco_string_create(L"\"--\" expected"); break;
			case 29: s = coco_string_create(L"\"if\" expected"); break;
			case 30: s = coco_string_create(L"\"else\" expected"); break;
			case 31: s = coco_string_create(L"\"unless\" expected"); break;
			case 32: s = coco_string_create(L"\"while\" expected"); break;
			case 33: s = coco_string_create(L"\"for\" expected"); break;
			case 34: s = coco_string_create(L"\"read\" expected"); break;
			case 35: s = coco_string_create(L"\"switch\" expected"); break;
			case 36: s = coco_string_create(L"\"case\" expected"); break;
			case 37: s = coco_string_create(L"\"break\" expected"); break;
			case 38: s = coco_string_create(L"\"default:\" expected"); break;
			case 39: s = coco_string_create(L"\"write\" expected"); break;
			case 40: s = coco_string_create(L"\"program\" expected"); break;
			case 41: s = coco_string_create(L"\"int\" expected"); break;
			case 42: s = coco_string_create(L"\"bool\" expected"); break;
			case 43: s = coco_string_create(L"\"char\" expected"); break;
			case 44: s = coco_string_create(L"\"const\" expected"); break;
			case 45: s = coco_string_create(L"\",\" expected"); break;
			case 46: s = coco_string_create(L"??? expected"); break;
			case 47: s = coco_string_create(L"invalid AddOp"); break;
			case 48: s = coco_string_create(L"invalid RelOp"); break;
			case 49: s = coco_string_create(L"invalid Factor"); break;
			case 50: s = coco_string_create(L"invalid MulOp"); break;
			case 51: s = coco_string_create(L"invalid Stat"); break;
			case 52: s = coco_string_create(L"invalid Stat"); break;
			case 53: s = coco_string_create(L"invalid Stat"); break;
			case 54: s = coco_string_create(L"invalid Type"); break;

		default:
		{
			wchar_t format[20];
			coco_swprintf(format, 20, L"error %d", n);
			s = coco_string_create(format);
		}
		break;
	}
	wprintf(L"-- line %d col %d: %ls\n", line, col, s);
	coco_string_delete(s);
	count++;
}

void Errors::Error(int line, int col, const wchar_t *s) {
	wprintf(L"-- line %d col %d: %ls\n", line, col, s);
	count++;
}

void Errors::Warning(int line, int col, const wchar_t *s) {
	wprintf(L"-- line %d col %d: %ls\n", line, col, s);
}

void Errors::Warning(const wchar_t *s) {
	wprintf(L"%ls\n", s);
}

void Errors::Exception(const wchar_t* s) {
	wprintf(L"%ls", s); 
	exit(1);
}

} // namespace

