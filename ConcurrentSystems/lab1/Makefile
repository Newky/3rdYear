all:build clean

build:
	gcc -o kdtree.o -c kdtree.c
	gcc -lm -O3 -o test kdtree.o sales.c

clean:
	rm -f *.o
