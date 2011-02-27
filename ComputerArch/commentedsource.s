	/*filename is declared*/
	.file	"ackerman.c"
	.text
	/*
	Declation of ackerman global function
	*/
.globl ackerman
	.type	ackerman, @function
ackerman:
	/*
	Save calling function's stack frame (i.e main)
	*/
	pushl	%ebp
	/*
	Make a new stack frame on top of the callers window.
	*/
	movl	%esp, %ebp
	/* allocate 40 bytes of local variable space */
	subl	$40, %esp
	/*
		Compare 0 with parameter x
	*/
	cmpl	$0, 8(%ebp)
	/*
		If not equal jump to the relevant label
	*/
	jne	.L2
	/*
		else move y+1 into return variable
		I have a return variable in my code 
		because I was using it to measure underflow overflow.
	*/
	movl	12(%ebp), %eax
	/* Add one to the result */
	addl	$1, %eax
	/*
		Move it into the return variable
		and jump to after the if else block
	*/
	movl	%eax, -12(%ebp)
	jmp	.L3
.L2:
	/*
		This label represents if x wasnt equal to 0
	*/
	cmpl	$0, 12(%ebp)
	/*
		if y wasnt equal to 0 jump to L4
	*/
	jne	.L4
	/*
		if y == 0
		move x into register eax
	*/
	movl	8(%ebp), %eax
	/*
		sub 1 from x (1st argument to ackerman function)
	*/
	subl	$1, %eax
	/* Move a 1 into the second argument of ackerman */
	movl	$1, 4(%esp)
	/* Move the first argument into the esp register which will be passed up */
	movl	%eax, (%esp)
	/* Call ackerman function with arguments and put return value into return variable and jump to end of if block. */
	call	ackerman
	/*
	Move the value into the return variable and jump out of if block
	*/
	movl	%eax, -12(%ebp)
	jmp	.L3
.L4:
	/* if x not equal to 0 and y not equal to 0 */
	/* prepare the arguments for the ackerman function 
	   x -1 and ackerman functon
	*/
	movl	12(%ebp), %eax
	subl	$1, %eax
	movl	%eax, 4(%esp)
	movl	8(%ebp), %eax
	movl	%eax, (%esp)
	call	ackerman
	movl	8(%ebp), %edx
	subl	$1, %edx
	movl	%eax, 4(%esp)
	movl	%edx, (%esp)
	call	ackerman
	/* Move return value of final ackerman into the return variable */
	movl	%eax, -12(%ebp)
.L3:
	/* move the return variable into the return register and leave and return from the function*/
	movl	-12(%ebp), %eax
	leave
	ret
	.size	ackerman, .-ackerman
/*Declaration of global main function*/
.globl main
	.type	main, @function
main:
/*
Save calling function's stack frame (i.e %ebp)
*/
	pushl	%ebp

/*
make a new stack frame on top of callers frame

*/
	movl	%esp, %ebp
	andl	$-16, %espa
/*
allocate 32 bytes for local variables.
*/
	subl	$32, %esp
/*
	Put the value 6 into the first parameter which has an offset of 4 bytes of esp, do the same with 3
*/
	movl	$6, 4(%esp)
	movl	$3, (%esp)
/*
Call ackerman
*/
	call	ackerman
/*
	%eax is the register that is usually used to store the return variable of a function, move this into r which is 28(%esp)
*/
	movl	%eax, 28(%esp)
	movl	$0, %eax
	leave
	ret
	.size	main, .-main
	.ident	"GCC: (Ubuntu 4.4.3-4ubuntu5) 4.4.3"
	.section	.note.GNU-stack,"",@progbits
