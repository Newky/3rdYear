#include <alloca.h>
#include <stdio.h>
#include <math.h>
#include <float.h>
#include <stdlib.h>

/* messing around with the array was really stupid */
const int DEBUG = 0;

typedef struct {
  double x;
  double y;
  /* try and think of a better way around this*/
  int pos; // this is thw position that it had in the ncities becuase you need to know this for filling in the tour at the end
} point;
// remember to ask someone why this isn't working 
// go for the easy option 
// this will be one of the problems the node must have the val as a pointer so this will take changing the code 
struct node {
  point* val;
  char visited; // this tells you if you've been to it already 
  struct node * left;
  struct node * right; 
};
typedef struct node Node;
void swap(Node *x,Node *y)
{
	Node temp;
	temp = *x;
	*x = *y;
	*y = temp;
}
/* make sure you get a good one theres no easy way to get a good middle for numbers !!!! 
   random choice isn't bad. but you know why its the 3 rule and not a varying number as the amount of entries increases to a massive amount
 so now you are just using 3 numbers to choose a starting pivot point */
int choose_pivot_on_x(Node* list,int m,int n ){ 	
	int i= (m+n)/2;
	if(list[m].val->x>list[n].val->x){
		if(list[m].val->x<list[i].val->x){
			return m;
		}else{ 	if(list[n].val->x>list[i].val->x){
				return n;
			}else{
				return i;
			}
		}
	}else{	if(list[n].val->x<list[i].val->x){
			return n;
		}else 	if(list[i].val->x<list[m].val->x){
				return m;
			}else{	
				return i;
			}
	}
}
 /* you are passing incorrect n's some of the time  */ 
int choose_pivot_on_y(Node* list,int m,int n )
{ 	int i= (m+n)/2;
	if(list[m].val->y>list[n].val->y){
		if(list[m].val->y<list[i].val->y){
			return m;
		}else{ if(list[n].val->y>list[i].val->y){
				return n;
			}else{ 
				return i;
			}
		}
	}else{	if(list[n].val->y<list[i].val->y){
			return n;
		}else{ 	if(list[i].val->y<list[m].val->y){
				return m;
			}else{	
				return i;
			}
		}
	}
}
// this method is a bit messy 
/* what this method does is it goes over all the        some of the error is occuring here ??? what is it


your program doesn't properly position the last one !!!  
 
 alright you can see extreme efficency  in your code if you don't actually sort but the nodes just point that would seriously kick ass 
 


 at the moment you sort the cities then make them into a node, it would be much better if you sorted the nodes based on the value in the points stored in
  the nodes, this better conceptually and might even make your code a tiny bit faster 
 */
int sort(Node list[],int m,int n, int chooser){
	int i,j,k;
	point key;
	if( m < n){
		if(chooser==1){
			k = choose_pivot_on_x(list,m,n);
		}else{
			k = choose_pivot_on_y(list,m,n);
		}
		/* its this next line of code thats causing it not to work the error i that it could move around the key !!! */
		/* so you know the problem now */
		swap(&list[m],&list[k]);
		key = *(list[m].val);
		i = m+1;
		j = n;
		if(chooser == 1){
			while(i <= j){
				while((i <= n) && (list[i].val->x <= key.x))
					i++;
				while((j >= m) && (list[j].val->x > key.x))
					j--;
				if( i < j)
					swap(&list[i],&list[j]);
				}
			swap(&list[m],&list[j]);
		}else{
			while(i <= j){
				while((i <= n) && (list[i].val->y <= key.y))
					i++;
				while((j >= m) && (list[j].val->y > key.y))
					j--;
				if( i < j)
					swap(&list[i],&list[j]);
			}
			swap(&list[m],&list[j]);
		}
		return j;
	}
} 
/* an important efficenciy thing here is to get it as one hugh malloc 
 the last parameter here is a pointer to a load of free space
 this method needs to be passed a load of malloc space already */
/* i finally got the error if you insert on one side  only you must put a null on the other side */
Node* buildTree(int depth,int m, int n,Node *root) {
	int temp = sort(root,m,n,(depth%2));
	int i; 
//	root[temp].val = cities+temp;
//	root[temp].visited = 0;
	i =m;
	if(m<n){
		if(m<=temp-1){
			root[temp].left = buildTree(depth+1 , m, temp-1, root);
		}else{
			root[temp].left = NULL;
		}
		if(temp+1<=n){
			root[temp].right= buildTree( depth+1,temp+1, n, root);
		}else{
			root[temp].right = NULL;
		}
	}else{
		if(m == n){
		root[temp].left = '\0'; 
		root[temp].right ='\0'; 
		}
	}
	return root+temp;
}
/* I think might what be crashing this is lack of symmetry  */ 
/* something is going wrong either here or before it !!!! your  create table is working under the assumption that the first one is gone */
void printNode(Node *root,int depth){
	if(root->left != NULL)
		printNode(root->left , depth +1);
	if(root->right != NULL)
		printNode(root->right, depth + 1);
}

double sqr(double x)
{
  return x*x;
}
/* dist is his one and its needed for the code */
double dist(const  point cities[], int i, int j) {
  return      sqr(cities[i].x-cities[j].x)+
	      sqr(cities[i].y-cities[j].y);
}
/* this is the one that you'll use */
double distance(point* city1,point* city2){
	return 	sqr(city1->x-city2->x)+
		sqr(city1->y-city2->y);
}
/* its no longer inefficent */
double distance_axis(Node *root,point current_city,int depth){
	if(depth%2==1){
		return	sqr(root->val->x-current_city.x);
	}else{
		return	sqr(root->val->y-current_city.y);
	}
}
Node* child_near(Node *root,point current_city,int depth){
	if(depth%2==0)
		if(root->val->y<current_city.y){
			return root->right;
		}else{
			return root->left;
	}else	
		if(root->val->x<current_city.x){
			return root->right;
		}else{
			return root->left;
		}
}
Node* far_child(Node *root,point current_city, int depth){
	if(depth%2==0){
		if(root->val->y>=current_city.y){
			return root->right;
		}else{
			return root->left;
		}
	}else{	
		if(root->val->x>=current_city.x){
			return root->right;
		}else{
			return root->left;
		}

	}
}
// root = the tree , current = the city you are currently in
// current_city . THIS IS NOT THE CURRENT NODE YOU ARE AT 
// best == the closest city you have found to current so far 
// this last parameter is used to tell you in which dimension to search 
// too this whole method I need to add not visited thing
// this is working properly it doesn't work with your visited idea

// alright so the problem is to do with the root being the number that you are searching for 
// it goes to the left god I'm soo smart !!! you could make them numbers that'll never be close like 70,70 
// you could remove this line of code and make a remove concept at the same time it could seriously improve speed 
// going to move that top line down so that it is checkouted
Node* searchTree(Node *root,point current_city,Node *best,int depth){
	if(root==NULL){
		return best;
	}
	// this line will always happen but the only time it'll stick is on a leaf node because for every other a child node will just be called and 
	// over write it 
	if(best==NULL && root->visited==0){
		best = root;
	}
	if((root->visited==0) &&(best==NULL||( distance(best->val,&current_city)>distance(root->val,&current_city)))){
		best = root;
	}
	if(root->left==NULL && root->right==NULL && root->visited==1){
		root=NULL;
	}else{
		Node* child = child_near(root,current_city,depth);
		best = searchTree(child,current_city,best,depth+1);

		if(best==NULL||(distance_axis(root,current_city,depth)<(distance(best->val,&current_city)))){
			child=far_child(root,current_city,depth);
			best= searchTree(child,current_city,best,depth+1);
		}
	}
	return best;
}
// just do this once !!!! I am the man !!!!!
// the problem earlier was that it was going out of bounds
/* one of the problems that you are going to face is getting the last!!! you can just search for it, that won't take that long or you could track it and then tag it as travelled to striaght away 
 the problems with doing this is that it'll be hard to keep track of 
   
the error could have been that one of hte indexes was too high 

 a better way would be to not insert it 
   you are not going to pass in the last city it'll make it easier 
 oh my god another hugh problem is writing out to the tour array each */
void my_find_tour( point cities[], int tour[], int ncities){
  	int i,j;
	
  	for(i=0;i<ncities;cities[i].pos=i++);
  	char *visited = alloca(ncities);
  	int ThisPt, ClosePt=0;
  	double CloseDist;
  	int endtour=0;
  	Node *tmp  =  malloc( (ncities-1)*sizeof(Node));
	for(i=ncities-1;i--;){
		tmp[i].val=cities+i;
		tmp[i].visited=0;
	}
  	Node * root =  buildTree(1,0,ncities-2,tmp);
  	ThisPt = ncities-1;
  	// you can remove the visited array I think 
  	visited[ncities-1] = 1;
  	tour[endtour] = ncities-1;
  	Node* temp;
  	// all this should really be in the loop 

  temp = searchTree(root,cities[ncities-1],NULL,1);
   // it doesn't assingn the last line  think about that you were being stupid !!!!!!!
  for(i=ncities-2;i--;){
	  temp->visited=1;
	  tour[++endtour]=temp->val->pos;
	  temp = searchTree(root,*(temp->val),NULL,1); // the problem is that this returned null !!!
  }

  temp->visited=1;
  tour[++endtour]=temp->val->pos;
}

void simple_find_tour(const point cities[], int tour[], int ncities)
{
  int i,j;
  char *visited = alloca(ncities);
  int ThisPt, ClosePt=0;
  double CloseDist;
  int endtour=0;
  
  for (i=0; i<ncities; i++)
    visited[i]=0;
  ThisPt = ncities-1;
  visited[ncities-1] = 1;
  tour[endtour++] = ncities-1;
  
  for (i=1; i<ncities; i++) {
    CloseDist = DBL_MAX;
    for (j=0; j<ncities-1; j++) {
      if (!visited[j]) {
	if (dist(cities, ThisPt, j) < CloseDist) {
	  CloseDist = dist(cities, ThisPt, j);
	  ClosePt = j;
	}
      }
    }
    tour[endtour++] = ClosePt;
    visited[ClosePt] = 1;
    ThisPt = ClosePt;
  }
}

/* write the tour out to console */
void write_tour(int ncities, point * cities, int * tour)
{
  int i;
  double sumdist = 0.0;

  /* write out the tour to the screen */
  printf("%d\n", tour[0]);
  for ( i = 1; i < ncities; i++ ) {
    printf("%d\n", tour[i]);
    sumdist += dist(cities, tour[i-1], tour[i]);
  }
  printf("sumdist = %f\n", sumdist);
}

/* write out an encapsulated postscript file of the tour */
void write_eps_file(int ncities, point *cities, int *tour)
{
  FILE *psfile;
  int i;

  psfile = fopen("sales.eps","w");
  fprintf(psfile, "%%!PS-Adobe-2.0 EPSF-1.2\n%%%%BoundingBox: 0 0 300 300\n");
  fprintf(psfile, "1 setlinejoin\n0 setlinewidth\n");
  fprintf(psfile, "%f %f moveto\n",
	  300.0*cities[tour[0]].x, 300.0*cities[tour[0]].y);
  for (i=1; i<ncities; i++) {
    fprintf(psfile, "%f %f lineto\n",
	    300.0*cities[tour[i]].x, 300.0*cities[tour[i]].y);
  }
  fprintf(psfile,"stroke\n");
}

/* create a random set of cities */
void initialize_cities(point * cities, int ncities, unsigned seed)
{
  int i;

  srandom(seed);
  for (i=0; i<ncities; i++) {
    cities[i].x = ((double)(random()))/(double)(1U<<31);
    cities[i].y = ((double)(random()))/(double)(1U<<31);
  }
}

int check_tour( point *cities, int * tour, int ncities)
{
  int * tour2 = malloc(ncities*sizeof(int));
  int i;
  int result = 1;

  simple_find_tour(cities,tour2,ncities);

  for ( i = 0; i < ncities; i++ ) {
    if ( tour[i] != tour2[i] ) {
      result = 0;
    }
  }
  free(tour2);
  return result;
}

/* include the C file containing your function to compute the tour.
   Note, using #include to include C code is not normally done */
/*#include "mytour.c"*/

void call_student_tour( point *cities, int * tour, int ncities)
{
  my_find_tour(cities, tour, ncities);
}

int main(int argc, char *argv[])
{
  int i, ncities;
  point *cities;
  int *tour;
  int seed;
  int tour_okay;
  struct timeval start_time, stop_time;
  long long compute_time;
  

  if (argc!=2) {
    fprintf(stderr, "usage: %s <ncities>\n", argv[0]);
    exit(1);
  }

  /* initialize random set of cities */
  ncities = atoi(argv[1]);
  cities = malloc(ncities*sizeof(point));
  tour = malloc(ncities*sizeof(int));
  seed = 3656384L % ncities;
  initialize_cities(cities, ncities, seed);
  /* find tour through the cities */
  gettimeofday(&start_time, NULL);
  call_student_tour(cities,tour,ncities);
  gettimeofday(&stop_time, NULL);
  compute_time = (stop_time.tv_sec - start_time.tv_sec) * 1000000L +
    (stop_time.tv_usec - start_time.tv_usec);
  printf("Time to find tour: %lld microseconds\n", compute_time);

  /* check that the tour we found is correct */
  tour_okay = check_tour(cities,tour,ncities);
  if ( !tour_okay ) {
    fprintf(stderr, "FATAL: incorrect tour\n");
  }
  /* write out results */
  if ( DEBUG ) {
    write_eps_file(ncities, cities, tour);
    write_tour(ncities, cities, tour);
  }
  free(cities);
  free(tour);
  return 0;
}
