proctype gcd(byte a, b){
	byte t;
	t=0;
	do
	:: b != 0 -> t = b; b = a % b; a = t;
	:: b == 0 -> printf("%d\n", a);break;
	od
}

init {
	run gcd(12, 16);
}
