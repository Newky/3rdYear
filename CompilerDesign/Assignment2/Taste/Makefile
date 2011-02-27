all:
	make grammer
	make compile
grammer:
	cococpp -namespace Taste Taste.atg
compile:
	g++ *.cpp -o Taste $(CFLAGS) 

clean:
	rm -f Taste

