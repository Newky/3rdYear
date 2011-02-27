	.file	"initial.c"
.globl g
	.data
	.align 4
	.type	g, @object
	.size	g, 4
g:
	.long	256
	.text
.globl p
	.type	p, @function
p:
	pushl	%ebp
	movl	%esp, %ebp
	subl	$16, %esp
	movl	12(%ebp), %eax
	movl	8(%ebp), %edx
	leal	(%edx,%eax), %eax
	movl	%eax, -4(%ebp)
	movl	-4(%ebp), %eax
	sall	$2, %eax
	subl	$1, %eax
	leave
	ret
	.size	p, .-p
.globl q
	.type	q, @function
q:
	pushl	%ebp
	movl	%esp, %ebp
	subl	$8, %esp
	movl	8(%ebp), %eax
	movl	%eax, %edx
	negl	%edx
	movl	g, %eax
	movl	%edx, 4(%esp)
	movl	%eax, (%esp)
	call	p
	leave
	ret
	.size	q, .-q
.globl f
	.type	f, @function
f:
	pushl	%ebp
	movl	%esp, %ebp
	subl	$24, %esp
	cmpl	$0, 8(%ebp)
	jle	.L6
	movl	8(%ebp), %eax
	subl	$1, %eax
	movl	%eax, (%esp)
	call	f
	imull	8(%ebp), %eax
	jmp	.L7
.L6:
	movl	$1, %eax
.L7:
	leave
	ret
	.size	f, .-f
.globl main
	.type	main, @function
main:
	pushl	%ebp
	movl	%esp, %ebp
	movl	$0, %eax
	popl	%ebp
	ret
	.size	main, .-main
	.ident	"GCC: (Ubuntu 4.4.3-4ubuntu5) 4.4.3"
	.section	.note.GNU-stack,"",@progbits
