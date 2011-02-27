#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <xmmintrin.h>

#define	SIZE	4096

/* Note this use of global variables is truly awful programming */
float	*vals;		//initial values
float	*res1, *res2; 	//results
float	a, b;

void cleanup()
{
  free(vals);
  free(res1);
  free(res2);
}

void init()
{
  int i;

  vals = (float *)memalign(16, sizeof(float) * SIZE);	
  res1 = (float *)memalign(16, sizeof(float) * SIZE);
  res2 = (float *)memalign(16, sizeof(float) * SIZE);	

  for(i=0; i<SIZE; i++){
    vals[i] = (float)random()/RAND_MAX;
  }

  a = 23;
  b = 132;
}

void sisd()
{
  int i;

  for(i = 0; i < SIZE; i++){
    float v = vals[i];
    res1[i] = 1.0f/a + (v+3.0f)*(v-4.0f) + (3.0f*v) + 1.0f/(sqrtf(v)) + b/a;
  }	
}



void simd()
{
  int i;
  __m128 vector_of_recipa = _mm_set1_ps(1.0f/a);
  __m128 vector_of_b_div_a = _mm_set1_ps(b/a);
  __m128 vector_of_one = _mm_set1_ps(1.0f);
  __m128 vector_of_two= _mm_set1_ps(2.0f);
  __m128 vector_of_three= _mm_set1_ps(3.0f);
  __m128 vector_of_four= _mm_set1_ps(4.0f);
  for(i = 0; i < SIZE; i+=4){
     __m128 v = _mm_load_ps(vals+i);
     __m128 temp = _mm_mul_ps(_mm_add_ps(v, vector_of_three), _mm_sub_ps(v, vector_of_four));
     temp = _mm_add_ps(temp, _mm_mul_ps(vector_of_three, v));
     temp = _mm_add_ps(temp, vector_of_recipa);
     temp = _mm_add_ps(temp, vector_of_b_div_a);
     temp = _mm_add_ps(temp, _mm_div_ps(vector_of_one, _mm_sqrt_ps(v)));
     _mm_store_ps(res2+i, temp);
  }
}

/* This is a special instruction that measure a time stamp
    counter that measures the number of elapsed clock ticks.
    This only works on x86 */
static inline unsigned long long ticks()
{
  return _rdtsc();
}

int main()
{
  int i;
  unsigned long long sisd_time, 
           simd_time;
  struct timeval start_time, middle_time, end_time;
  init();

  gettimeofday(&start_time, NULL);
  sisd();
  gettimeofday(&middle_time, NULL);
  simd();
  gettimeofday(&end_time, NULL);

  printf("Dumping results\n\n");

  for( i = 0; i < SIZE; i++ ){
    printf("%f\t%f\t%f\n", vals[i], res1[i], res2[i]);	
  }

  sisd_time = (middle_time.tv_sec - start_time.tv_sec) * 1000000L +(middle_time.tv_usec - start_time.tv_usec);
  simd_time = (end_time.tv_sec - middle_time.tv_sec) * 1000000L +(end_time.tv_usec - middle_time.tv_usec);

  printf("sisd time was %lld\n", sisd_time);
  printf("simd time was %lld\n", simd_time);
  printf("SIMD time was %f%% of SISD time\n", 
         ((float)simd_time/(float)sisd_time)*100.0f);

  cleanup();
  return 0;
}
