/*
 * 3BA26 Mandelbrot Project
 * 
 * Using techniques we've covered in class, accelerate the rendering of
 * the M set.
 * 
 * Hints
 * 
 * 1) Vectorize
 * 2) Use threads
 * 3) Load Balance
 * 4) Profile and Optimise
 * 
 * Potential FAQ.
 * 
 * Q1) Why when I zoom in far while palying with the code, why does the image begin to render all blocky?
 * A1) In order to render at increasing depths we must use increasingly higher precision floats
 * 	   We quickly run out of precision with 32 bits floats. Change all floats to doubles if you want
 * 	   dive deeper. Eventually you will however run out of precision again and need to integrate an
 * 	   infinite precision math library or use other techniques.
 * 
 * Q2) Why do some frames render much faster than others?
 * A2) Frames with a lot of black, i.e, frames showing a lot of set M, show pixels that ran until the 
 *     maximum number of iterations was reached before bailout. This means more CPU time was consumed
 */


#include <iostream>
#include <cmath>
#include <omp.h>


#include "Screen.h"
#include <sys/time.h>
#include <stdlib.h>

#include <xmmintrin.h>
/*
 * You can't change these values to accelerate the rendering.
 * Feel free to play with them to render different images though.
 */
const int 	MAX_ITS = 1000;			//Max Iterations before we assume the point will not escape
const int 	GREGG= 0;			//Max Iterations before we assume the point will not escape
const int 	HXRES = 700; 			// horizontal resolution	
const int 	HYRES = 700;			// vertical resolution
const int 	NUM_PIXELS= HXRES * HYRES;			// vertical resolution
const int 	MAX_DEPTH = 30;		// max depth of zoom
const float ZOOM_FACTOR = 1.02;		// zoom between each frame

/* Change these to zoom into different parts of the image */
const float PX = -0.702295281061;	// Centre point we'll zoom on - Real component
const float PY = +0.350220783400;	// Imaginary component


/*
 * The palette. Modifying this can produce some really interesting renders.
 * The colours are arranged R1,G1,B1, R2, G2, B2, R3.... etc.
 * RGB values are 0 to 255 with 0 being darkest and 255 brightest
 * 0,0,0 is black
 * 255,255,255 is white
 * 255,0,0 is bright red
 */
unsigned char pal[]={
	255,180,4,
	240,156,4,
	220,124,4,
	156,71,4,
	72,20,4,
	251,180,4,
	180,74,4,
	180,70,4,
	164,91,4,
	100,28,4,
	191,82,4,
	47,5,4,
	138,39,4,
	81,27,4,
	192,89,4,
	61,27,4,
	216,148,4,
	71,14,4,
	142,48,4,
	196,102,4,
	58,9,4,
	132,45,4,
	95,15,4,
	92,21,4,
	166,59,4,
	244,178,4,
	194,121,4,
	120,41,4,
	53,14,4,
	80,15,4,
	23,3,4,
	249,204,4,
	97,25,4,
	124,30,4,
	151,57,4,
	104,36,4,
	239,171,4,
	131,57,4,
	111,23,4,
	4,2,4};
const int PAL_SIZE = 40;  //Number of entries in the palette 



/* 
 * Return true if the point cx,cy is a member of set M.
 * iterations is set to the number of iterations until escape.
 */
bool member(float cx, float cy, int& iterations)
{
	float x = 0.0;
	float y = 0.0;
	iterations = 0;
	while ((x*x + y*y < (2*2)) && (iterations < MAX_ITS)) {
		float xtemp = x*x - y*y + cx;
		y = 2*x*y + cy;
		x = xtemp;
		iterations++;
	}

	return (iterations == MAX_ITS);
}

__m128 member_speed(__m128 cx_m , __m128 cy_m)
{
	__m128 x = _mm_set1_ps(0.0f);
	__m128 y = _mm_set1_ps(0.0f);
	__m128 four_iter = _mm_set1_ps(0.0);
	__m128 temp_mask = _mm_set1_ps(0.0);
	__m128 mask = _mm_set1_ps(1.0);
	__m128 two_squared = _mm_set1_ps(4.0f);
	__m128 two = _mm_set1_ps(2.0f);
	__m128 x_sqr, y_sqr;
	x_sqr = _mm_mul_ps(x, x);
	y_sqr = _mm_mul_ps(y, y);
	int iterations = 0;
	while ( (_mm_movemask_ps( temp_mask = _mm_cmplt_ps(_mm_add_ps(x_sqr, y_sqr),two_squared)) != 0)  && (iterations < MAX_ITS) ){
		__m128 xtemp = _mm_add_ps(_mm_sub_ps(x_sqr, y_sqr), cx_m);
		y = _mm_add_ps(_mm_mul_ps(two, _mm_mul_ps(x, y)), cy_m);
		x = xtemp;
		x_sqr = _mm_mul_ps(x, x);
		y_sqr = _mm_mul_ps(y, y);
		iterations ++;
		four_iter = _mm_add_ps(four_iter, _mm_and_ps(temp_mask, mask));
	}
	return four_iter;
}



int main()
{	

	float m=1.0; /* initial  magnification		*/

	/* Timing variables */
	struct timeval start_time, stop_time;
	long long compute_time;

	/* 			*/
	/* Create a screen to render to */
	Screen *screen;
	screen = new Screen(HXRES, HYRES);
	gettimeofday(&start_time, NULL);
	int depth=0;
	//#pragma omp parallel
	//{
	while (depth < MAX_DEPTH) {
		if(GREGG){	
			int hx, hy;
			for (hy=0; hy<HYRES; hy++) {
				for (hx=0; hx<HXRES; hx++) {
					int iterations;

					/* 
					 * Translate pixel coordinates to complex plane coordinates centred
					 * on PX, PY
					 */
					float cx = ((((float)hx/(float)HXRES) -0.5 + (PX/(4.0/m)))*(4.0f/m));
					float cy = ((((float)hy/(float)HYRES) -0.5 + (PY/(4.0/m)))*(4.0f/m));
					//__m128 cx_m = _mm_set1_ps(cx);
					//__m128 cy_m = _mm_set1_ps(cy);
					//if (!member_speed(cx_m, cy_m, iterations)) 
					if (!member(cx, cy, iterations)) {
						/* Point is not a member, colour based on number of iterations before escape */
						int i=(iterations%40) - 1;
						int b = i*3;
						screen->putpixel(hx, hy, pal[b], pal[b+1], pal[b+2]);
					} else {
						/* Point is a member, colour it black */
						screen->putpixel(hx, hy, 0, 0, 0);
					}
				}
			}
		}
		else{
			#pragma omp parallel
			{
			float * answers = (float *)malloc(sizeof(float) * 4);
			#pragma omp for schedule(dynamic)
			for (int hy=0; hy<HYRES; hy++) {
				float cy = ((((float)hy/(float)HYRES) -0.5 + (PY/(4.0/m)))*(4.0f/m));
				__m128 cy_m = _mm_set1_ps(cy);
				//__m128 cy_m = _mm_setr_ps(hy, hy+1, hy+2, hy+3);
				__m128 four_m = _mm_set1_ps((4.0/m));
				//cy_m = _mm_div_ps(cy_m, _mm_set1_ps(HYRES));
				//cy_m = _mm_sub_ps(cy_m, _mm_set1_ps(0.5));
				//cy_m = _mm_add_ps(cy_m, _mm_mul_ps(_mm_div_ps(_mm_set1_ps(PY),four_m), four_m));
				for (int hx=0; hx<HXRES; hx+=4) {
					//float cx = ((((float)hx/(float)HXRES) -0.5 + (PX/(4.0/m)))*(4.0f/m));
					//float cy = ((((float)hy/(float)HYRES) -0.5 + (PY/(4.0/m)))*(4.0f/m));
					__m128 cx_m = _mm_setr_ps(hx, hx+1, hx+2, hx+3);
					cx_m = _mm_div_ps(cx_m, _mm_set1_ps(HXRES));
					cx_m = _mm_sub_ps(cx_m, _mm_set1_ps(0.5));
					cx_m = _mm_add_ps(cx_m, _mm_div_ps(_mm_set1_ps(PX),four_m));
					cx_m = _mm_mul_ps(cx_m, four_m);

					_mm_storeu_ps(answers, member_speed(cx_m, cy_m));
					for (int k = 0; k < 4; k++) {
						if (answers[k] != MAX_ITS) {
							int l=((int)(answers[k]) % 40) - 1;
							l = l*3;
							screen->putpixel(hx+k, hy, pal[l], pal[l+1], pal[l+2]);
						}else{
							screen->putpixel(hx+k, hy, 0, 0, 0);
						}
					}
				}
			}
			}
		}
		
		screen->flip();
		/* Show the rendered image on the screen */
		std::cerr << "Render done " << depth++ << " " << m << std::endl;
		/* Zoom in */
		m *= ZOOM_FACTOR;
	}
	//}
	gettimeofday(&stop_time, NULL);
	compute_time = (stop_time.tv_sec - start_time.tv_sec) * 1000000L +
		(stop_time.tv_usec - start_time.tv_usec);
	fprintf(stderr, "Time to find Richys tour: %lld microseconds\n", compute_time);
	sleep(5);
	std::cout << "Clean Exit"<< std::endl;

}
