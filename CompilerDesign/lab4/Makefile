all:build clean

build:
	g++ -o SymbolTable.o -c SymbolTable.cpp
	g++ -o Parser.o -c Parser.cpp
	g++ -o Scanner.o -c Scanner.cpp
	g++ -o Taste.o -c Taste.cpp
	g++ -g Taste.o Scanner.o Parser.o SymbolTable.o  -o test
clean:
	rm -rf *.o
