

#if !defined(COCO_PARSER_H__)
#define COCO_PARSER_H__

#include "SymbolTable.h"
#include "CodeGenerator.h"
#include "wchar.h"


#include "Scanner.h"

namespace Taste {


class Errors {
public:
	int count;			// number of errors detected

	Errors();
	void SynErr(int line, int col, int n);
	void Error(int line, int col, const wchar_t *s);
	void Warning(int line, int col, const wchar_t *s);
	void Warning(const wchar_t *s);
	void Exception(const wchar_t *s);

}; // Errors

class Parser {
private:
	enum {
		_EOF=0,
		_ident=1,
		_number=2
	};
	int maxT;

	Token *dummyToken;
	int errDist;
	int minErrDist;

	void SynErr(int n);
	void Get();
	void Expect(int n);
	bool StartOf(int s);
	void ExpectWeak(int n, int follow);
	bool WeakSeparator(int n, int syFol, int repFol);

public:
	Scanner *scanner;
	Errors  *errors;

	Token *t;			// last recognized token
	Token *la;			// lookahead token

int // operators
	  plus, minus, times, slash, equ, lss, gtr;

	int // types
	  undef, integer, boolean;

	int // object kinds
	  var, proc;

	int // opcodes
	  ADD,  SUB,   MUL,   DIV,   EQU,  LSS, GTR, NEG,
	  LOAD, LOADG, STO,   STOG,  CONST,
	  CALL, RET,   ENTER, LEAVE,
	  JMP,  FJMP,  READ,  WRITE; 
	
	SymbolTable   *tab;
	CodeGenerator *gen;

	void Err(wchar_t* msg) {
		errors->Error(la->line, la->col, msg);
	}

	void InitDeclarations() { // it must exist
		plus = 0; minus = 1; times = 2; slash = 3; equ = 4; lss = 5; gtr = 6; // operators
		undef = 0; integer = 1; boolean = 2; // types
		var = 0; proc = 1; // object kinds

		// opcodes
		ADD  =  0; SUB   =  1; MUL   =  2; DIV   =  3; EQU   =  4; LSS = 5; GTR = 6; NEG = 7;
		LOAD =  8; LOADG =  9; STO   = 10; STOG  = 11; CONST = 12;
		CALL = 13; RET   = 14; ENTER = 15; LEAVE = 16;
		JMP  = 17; FJMP  = 18; READ  = 19; WRITE = 20;
	}


  
/*--------------------------------------------------------------------------*/


	Parser(Scanner *scanner);
	~Parser();
	void SemErr(const wchar_t* msg);

	void AddOp(int &op);
	void Expr(int &type);
	void SimExpr(int &type);
	void RelOp(int &op);
	void Factor(int &type);
	void Ident(wchar_t* &name);
	void MulOp(int &op);
	void ProcDecl();
	void VarDecl();
	void Stat();
	void Term(int &type);
	void Taste();
	void Type(int &type);

	void Parse();

}; // end Parser

} // namespace


#endif // !defined(COCO_PARSER_H__)

