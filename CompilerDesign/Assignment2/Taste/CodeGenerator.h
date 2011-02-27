#if !defined(TASTE_CODEGENERATOR_H__)
#define TASTE_CODEGENERATOR_H__

#include "Scanner.h"
#include <stdlib.h>
#include <stdio.h>
#include <wchar.h>

namespace Taste {

class CodeGenerator
{
public:
	// opcodes
	int
	  ADD,  SUB,   MUL,   DIV,   EQU,  LSS, GTR, NEG, NE, LEQ,GEQ,
	  LOAD, LOADG, STO,   STOG,  CONST,
	  CALL, RET,   ENTER, LEAVE,
	  JMP,  FJMP,  READ,  WRITE, WRITEC;

	wchar_t* opcode[25];

	int progStart;		// address of first instruction of main program
	int pc;				// program counter
	char *code;

	// data for Interpret
	int *globals;
	int *stack;
	int top;	// top of stack
	int bp;		// base pointer


	CodeGenerator() {
		// opcodes
		ADD  =  0; SUB   =  1; MUL   =  2; DIV   =  3; EQU   =  4; LSS = 5; GTR = 6; NEG = 7; NE = 8;LEQ = 9;GEQ = 10;
		LOAD =  11; LOADG =  12; STO   = 13; STOG  = 14; CONST = 15;
		CALL = 16; RET   = 17; ENTER = 18; LEAVE = 19;
		JMP  = 20; FJMP  = 21; READ  = 22; WRITE = 23; WRITEC = 24;

		opcode[ 0] = coco_string_create("ADD  ");
		opcode[ 1] = coco_string_create("SUB  ");
		opcode[ 2] = coco_string_create("MUL  ");
		opcode[ 3] = coco_string_create("DIV  ");
		opcode[ 4] = coco_string_create("EQU  ");
		opcode[ 5] = coco_string_create("LSS  ");
		opcode[ 6] = coco_string_create("GTR  ");
		opcode[ 7] = coco_string_create("NEG  ");
		opcode[ 8] = coco_string_create("NE   ");
		opcode[ 9] = coco_string_create("LEQ  ");
		opcode[ 10] = coco_string_create("GEQ  ");
		opcode[ 11] = coco_string_create("LOAD ");
		opcode[ 12] = coco_string_create("LOADG");
		opcode[13] = coco_string_create("STO  ");
		opcode[14] = coco_string_create("STOG ");
		opcode[15] = coco_string_create("CONST");
		opcode[16] = coco_string_create("CALL ");
		opcode[17] = coco_string_create("RET  ");
		opcode[18] = coco_string_create("ENTER");
		opcode[19] = coco_string_create("LEAVE");
		opcode[20] = coco_string_create("JMP  ");
		opcode[21] = coco_string_create("FJMP ");
		opcode[22] = coco_string_create("READ ");
		opcode[23] = coco_string_create("WRITE");
		opcode[24] = coco_string_create("WRITEC");

		code    = new char[3000];
		globals = new int[100];
		stack   = new int[100];

		progStart = 0;

		pc = 1;
	}

	~CodeGenerator() {
		coco_string_delete(opcode[ 0]);
		coco_string_delete(opcode[ 1]);
		coco_string_delete(opcode[ 2]);
		coco_string_delete(opcode[ 3]);
		coco_string_delete(opcode[ 4]);
		coco_string_delete(opcode[ 5]);
		coco_string_delete(opcode[ 6]);
		coco_string_delete(opcode[ 7]);
		coco_string_delete(opcode[ 8]);
		coco_string_delete(opcode[ 9]);
		coco_string_delete(opcode[10]);
		coco_string_delete(opcode[11]);
		coco_string_delete(opcode[12]);
		coco_string_delete(opcode[13]);
		coco_string_delete(opcode[14]);
		coco_string_delete(opcode[15]);
		coco_string_delete(opcode[16]);
		coco_string_delete(opcode[17]);
		coco_string_delete(opcode[18]);
		coco_string_delete(opcode[19]);
		coco_string_delete(opcode[20]);
		coco_string_delete(opcode[21]);
		coco_string_delete(opcode[22]);
		coco_string_delete(opcode[23]);
		coco_string_delete(opcode[24]);
	}

	//----- code generation methods -----

	void Emit (int op) {
		code[pc++] = (char)op;
	}

	void Emit (int op, int val) {
		Emit(op); Emit(val>>8); Emit(val);
	}

	void Patch (int adr, int val) {
		code[adr] = (char)(val>>8); code[adr+1] = (char)val;
	}

	void Decode() {
		int maxPc = pc;
		pc = 1;
		while (pc < maxPc) {
			int code = Next();
			printf("%3d: %s ", pc-1, coco_string_create_char(opcode[code]));
			if (code == LOAD || code == LOADG || code == CONST || code == STO || code == STOG ||
				code == CALL || code == ENTER || code == JMP   || code == FJMP)
					printf("%d\n", Next2());
			else
			if (code == ADD  || code == SUB || code == MUL || code == DIV || code == NEG || code == NE || code == LEQ ||
				code == GEQ || code == EQU  || code == LSS || code == GTR || code == RET || code == LEAVE ||
				code == READ || code == WRITE || code == WRITEC)
					printf("\n");
		}
	}

	//----- interpreter methods -----

	int Next () {
		return code[pc++];
	}

	int Next2 () {
		int x,y;
		x = (char)code[pc++]; y = code[pc++];
		return (x << 8) + y;
	}

	int Int (bool b) {
		if (b) return 1; else return 0;
	}

	void Push (int val) {
		stack[top++] = val;
	}

	int Pop() {
		return stack[--top];
	}

	int ReadInt(FILE* s) {
		int sign;
		char ch;
		do {fscanf(s, "%c", &ch);} while (!(ch >= '0' && ch <= '9' || ch == '-'));

		if (ch == '-') {sign = -1; fscanf(s, "%c", &ch);} else sign = 1;
		int n = 0;
		while (ch >= '0' && ch <= '9') {
			n = 10 * n + (ch - '0');
			if (fscanf(s, "%c", &ch) <= 0)
				break;
		}
		return n * sign;
	}

	void Interpret (char* data) {
		int val;
		FILE* s;
		if ((s = fopen(data, "r")) == NULL) {
			printf("--- Error accessing file %s\n", (char*)data);
			exit(1);
		}
		printf("\n");
		pc = progStart; stack[0] = 0; top = 1; bp = 0;
		for (;;) {
			int nxt = Next();
			if (nxt == CONST)
				Push(Next2());
			else if (nxt == LOAD)
				Push(stack[bp+Next2()]);
			else if (nxt == LOADG)
				Push(globals[Next2()]);
			else if (nxt == STO)
				stack[bp+Next2()] = Pop();
			else if (nxt == STOG)
				globals[Next2()] = Pop();
			else if (nxt == ADD)
				Push(Pop()+Pop());
			else if (nxt == SUB)
				Push(Pop()-Pop());
			else if (nxt == DIV)
				{val = Pop(); Push(Pop()/val);}
			else if (nxt == MUL)
				Push(Pop()*Pop());
			else if (nxt == NEG)
				Push(-Pop());
			else if (nxt == NE)
				Push(Int(Pop()!=Pop()));
			else if (nxt == LEQ)
				Push(Int(Pop()>=Pop()));
			else if (nxt == GEQ)
				Push(Int(Pop()<=Pop()));
			else if (nxt == EQU)
				Push(Int(Pop()==Pop()));
			else if (nxt == LSS)
				Push(Int(Pop()>Pop()));
			else if (nxt == GTR)
				Push(Int(Pop()<Pop()));
			else if (nxt == JMP)
				pc = Next2();
			else if (nxt == FJMP)
				{ val = Next2(); if (Pop()==0) pc = val;}
			else if (nxt == READ)
				{val = ReadInt(s); Push(val);}
			else if (nxt == WRITE)
				printf("%d\n", Pop());
			else if (nxt == WRITEC)
				printf("%c\n", Pop());
			else if (nxt == CALL)
				{Push(pc+2); pc = Next2();}
			else if (nxt == RET)
				{pc = Pop(); if (pc == 0) return;}
			else if (nxt == ENTER)
				{Push(bp); bp = top; top = top + Next2();}
			else if (nxt == LEAVE)
				{top = bp; bp = Pop();}
			else {
				printf("illegal opcode \n");
				exit(1);
			}
		}
	}

};

}; // namespace

#endif // !defined(TASTE_CODEGENERATOR_H__)
