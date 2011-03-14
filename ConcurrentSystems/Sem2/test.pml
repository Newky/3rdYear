proctype gcd(int a, b){
	int t;
	t=0;
	do
	:: b != 0 -> t = b; b = a % b; a = t;
	:: b == 0 -> printf("%d\n", a);break;
	od
}

init {
	run gcd(2322, 654);
}
