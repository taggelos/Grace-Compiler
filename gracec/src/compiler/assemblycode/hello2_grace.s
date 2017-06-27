.intel_syntax noprefix
.text
	.global main
main:
	push ebp
	mov ebp, esp
	sub esp, 8

	mov DWORD PTR [ebp - 4], 4
	mov ecx, 1 
	mov eax, 4 
	imul eax, ecx
	mov DWORD PTR [ebp-12], eax
	mov eax, DWORD PTR [ebp - 12]
	mov DWORD PTR [ebp - 4], eax
	mov eax, DWORD PTR [ebp - 4]
	push eax
	lea esi, DWORD PTR [ebp - 12]
	push esi
	push ebp
	call grace_f 
	add esp, 8
	mov eax, DWORD PTR [ebp - 12]
	mov DWORD PTR [ebp - 8], eax
	mov ecx, 2 
	mov eax, DWORD PTR [ebp - 8]
	add eax, ecx
	mov DWORD PTR [ebp-16], eax
	mov eax, DWORD PTR [ebp - 16]
	mov DWORD PTR [ebp - 8], eax
	mov eax, DWORD PTR [ebp - 8]
	push eax
	sub esp, 4
	push ebp
	call grace_puti 
	add esp, 8

endof_main:
	mov esp, ebp
	pop ebp
	ret
grace_f :
	push ebp
	mov ebp, esp
	sub esp, 4

	mov DWORD PTR [ebp + 16], 5
	mov eax, DWORD PTR [ebp + 16]
	push eax
	sub esp, 4
	push ebp
	call grace_puti 
	add esp, 8
	mov esi, DWORD PTR [ebp + 12]
	mov DWORD PTR [esi], 2
	jmp endof_f 

endof_f :
	mov esp, ebp
	pop ebp
	ret
grace_puti:	push ebp
	mov ebp, esp
	
	push eax
	mov eax, OFFSET FLAT:int_fmt
	push eax
	call printf
	add esp, 8
	
	mov esp, ebp
	pop ebp
	ret
	
.data
	int_fmt: .asciz  "%d"
