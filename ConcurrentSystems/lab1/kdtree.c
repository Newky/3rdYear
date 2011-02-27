#include<stdio.h>
#include<stdlib.h>
#include<float.h>
#include "kdtree.h"


double sqr(double x)
{
  return x*x;
}

double dist(const point cities[], int i, int j) {
	return sqr(cities[i].x - cities[j].x) + sqr(cities[i].y - cities[j].y);
}

int point_cmp_by_x(const void * a, const void * b)
{
	struct point *ia = (struct point *) a;
	struct point *ib = (struct point *) b;

	if (ia->x > ib->x)
		return 1;
	else if (ia->x < ib->x)
		return -1;
	else
		return 0;
}
int point_cmp_by_y(const void * a, const void * b)
{
	struct point *ia = (struct point *) a;
	struct point *ib = (struct point *) b;

	if (ia->y > ib->y)
		return 1;
	else if (ia->y < ib->y)
		return -1;
	else
		return 0;
}

void swap(point cities[], int x, int y)
{
	struct point temp = cities[x];
	cities[x] = cities[y];
	cities[y] = temp;
}
void print_array(point cities[], int n){
	int i=0;
	for (i = 0; i < n; i++) {
		printf("%lf %lf \n", cities[i].x, cities[i].y);
	}
}
int partition(point cities[], int low, int high, int axis)
{
	if(high-low <= 1)
		return 0;
	int left, right;
	double pivot;
	int piv = ((double)(high-low)/2);
	if(axis == 0)
		pivot = cities[piv].x;
	else
		pivot = cities[piv].y;
	/*printf("%d %lf\n", piv, cities[piv].y);*/
	left = low;
	right= high-1;
	while(1){
		if(axis == 0)
		{
			while(cities[left].x <= pivot && left <= high-1)
				left++;
			while(cities[right].x > pivot)
				right--;
		}
		if(axis == 1)
		{
			while(cities[left].y<= pivot && left <= high-1)
				left++;
			while(cities[right].y > pivot)
				right--;
		}
		if(left >= right) break;
		swap(cities, left, right);
		left++;
		right--;
	}
	/*printf("AFTER SORT %d %d\n" ,left, right);*/
	/*swap(cities, low, right);*/
	/*print_array(cities, high);*/
	/*printf("AFTER SWAP %d\n", right);*/
	return right;
}



int test(point cities[],int n, int depth){
	print_array(cities, n);
	int median =partition(cities, 0, n, 0);
	printf("Median%d %lf\n", median, cities[median].x);
	print_array(cities, n);
	printf("\n\n");
	print_array(cities, median);
	int new_median = partition(cities+median+1, 0, n-median-1, 1);
	printf("Median%d %lf\n", new_median, cities[median+1+new_median].y);

	return 0;
}

node * kd_tree(point cities[],int n, int depth){
	if(n == 0)
		return NULL;
	else
	{
		if(depth == 0)
			qsort(cities, n, sizeof(struct point), point_cmp_by_x);
		else
			qsort(cities, n, sizeof(struct point), point_cmp_by_y);
		int median = (double) n/2;
		node * new_node = malloc(sizeof(node));
		new_node->value = (depth == 0) ? cities[median].x:cities[median].y;
		new_node->axis = depth;
		new_node->city_no = cities[median].pos;
		new_node->left = kd_tree(cities, median, !depth);
		/*printf("Median%d %lf\n", median, cities[median].x);*/
		/*print_array(cities, median);*/
		new_node->right = kd_tree(cities+median+1, n-median-1, !depth);
		/*printf("Median%d %lf\n", median, cities[median].x);*/
		/*print_array(cities+median+1, n-median-1);*/
		return new_node;
	}
}

void check_subtree(node * head, point cities[], int pointIndex, int * nn, double * dmin, char * visited){
	if(head == NULL)
		return;
	double d=0;
	if(visited[head->city_no] != 1)
	{

		d= dist(cities, head->city_no, pointIndex);
		
		if(*dmin == -1 ||(d < *dmin && head->city_no != pointIndex))
		{
			*dmin = d;
			*nn = head->city_no;
		}

	}
	int axis = head->axis;
	if(axis == 0)
	{
		if(cities[pointIndex].x < head->value)
			check_subtree(head->left, cities, pointIndex, nn, dmin, visited);
		else
			check_subtree(head->right, cities, pointIndex, nn, dmin, visited);
	}
	else
	{
		if(cities[pointIndex].y < head->value)
			check_subtree(head->left, cities, pointIndex, nn, dmin, visited);
		else
			check_subtree(head->right, cities, pointIndex, nn,dmin, visited);
	}
	d = (axis == 0)? cities[head->city_no].x - cities[pointIndex].x:cities[head->city_no].y - cities[pointIndex].y;
	if(*dmin == -1 || sqr(d) < *dmin){
		if(axis == 0)
		{
			if(cities[pointIndex].x < head->value)
				check_subtree(head->right, cities, pointIndex, nn, dmin, visited);
			else
				check_subtree(head->left, cities, pointIndex, nn, dmin, visited);
		}
		else
		{
			if(cities[pointIndex].y < head->value)
				check_subtree(head->right, cities, pointIndex, nn, dmin, visited);
			else
				check_subtree(head->left, cities, pointIndex, nn,dmin, visited);
		}
	}
}

/* searching the tree calls the check_subtree method */
int search_tree_for_nearest(point cities[], int pointIndex, node * head, char * visited)
{
	/*Pseudo Code*/
	int nn = head->city_no; 
	double d_min = -1;
	int * nnp = &nn;
	double * dd_min = &d_min;
	check_subtree(head, cities, pointIndex, nnp, dd_min, visited);
	return nn;
}

