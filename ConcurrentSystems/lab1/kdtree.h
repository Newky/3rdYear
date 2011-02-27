typedef struct point{
  double x;
  double y;
  int pos;
} point;

typedef struct node{
double value;
int axis;
int city_no;
int visited;
point * p;
struct node * right;
struct node * left;
} node;

int partition(point cities[], int low, int high, int axis);

node * kd_tree(point cities[],int n, int depth);

int search_tree_for_nearest(point cities[], int pointIndex, node * head, char * visited);

int is_leaf(node * n);

double sqr(double x);

double dist(const point cities[], int i, int j);
