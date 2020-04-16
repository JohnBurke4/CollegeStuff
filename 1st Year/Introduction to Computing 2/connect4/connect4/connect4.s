;
; CS1022 Introduction to Computing II 2018/2019
; Mid-Term Assignment - Connect 4 - SOLUTION
;
; get, put and puts subroutines provided by jones@scss.tcd.ie
;

PINSEL0	EQU	0xE002C000
U0RBR	EQU	0xE000C000
U0THR	EQU	0xE000C000
U0LCR	EQU	0xE000C00C
U0LSR	EQU	0xE000C014
	
SIZE 	EQU 0x2A
ROWS	EQU 0x06
COLS	EQU 0x07	


	AREA	globals, DATA, READWRITE
BOARD	DCB	0,0,0,0,0,0,0
	DCB	0,0,0,0,0,0,0
	DCB	0,0,0,0,0,0,0
	DCB	0,0,0,0,0,0,0
	DCB	0,0,0,0,0,0,0
	DCB	0,0,0,0,0,0,0


	AREA	RESET, CODE, READONLY
	ENTRY

	; initialise SP to top of RAM

	LDR	R13, =0x40010000	; initialse SP

	; initialise the console
	BL	inithw

	;
	; your program goes here
	;
	
	; initilising some variables
	LDR R8, =0x58		; player1 = X
	LDR R9, =0x4F		; player2 = O
	LDR R10, =0x0		; gameOver = false
	MOV R11, R8			; currentMove = X
	LDR R12, =2			; computer last move = 3
	LDR R6, =0			; movesMade = 0
	LDR R7, =0			; computer = true;
	LDR R0, =str_go
	
	BL puts		;
	LDR R0, =str_game
	BL puts
	BL get		; check if player wants a human or computer opponent
	CMP R0, #0x31	; if 1 play against human otherwise not
	BNE L110
	MOV R7, #1
	
	
L110
	LDR R0, =BOARD	;
	LDR R1, =SIZE	;
	BL initBoard		; initise board
	
	LDR R0, =BOARD	;
	LDR R1, = SIZE
	BL disBoard			; display board
	
L4	CMP R10, #0x0	; while !gameOver
	BNE L2
	
L3	MOV R0, R11
	BL disMove		; display whose move it is to console
	
	CMP R7, #0
	BNE L112
	CMP R11, #0x4F
	BEQ L100
L112
	BL takeMove		; ask the user to make a move
	MOV R4, R0
	
	MOV R0, R4
	LDR R1, =BOARD
	BL checkMove	; check if there is space to make that mvoe
	CMP R0, #1
	BNE L1
L101
	MOV R0, R11
	MOV R1, R4
	LDR R2, =BOARD
	BL makeMove		; if space make the move
	MOV R5, R0
	
	LDR R0, =BOARD	;
	LDR R1, = SIZE
	BL disBoard		; display the board
	
	
	MOV R0, R11
	MOV R1, R4
	MOV R2, R5
	LDR R3, =BOARD
	BL checkWin		; check if move was winning move
	CMP R0, #1 
	BEQ L7
	
L8	ADD R6, R6, #1	; movesMade++
	CMP R6, #SIZE	; is board full?
	BGE L20
	CMP R11, R8 	; check and change whose turn it is
	BEQ L5
	MOV R11, R8
	B L6
L5  MOV R11, R9
L6	B L4
L1
	LDR R0, =str_tryAgain	; if illegal move display so
	BL puts
	LDR R0, =BOARD	;
	LDR R1, = SIZE
	BL disBoard				; display move
	
	B L3
	
L7
	MOV R0, R11
	BL disWinner			; if winner display so
	MOV R10, #1
	B L8

L20
	LDR R0, =str_draw		; if board is full display draw
	BL puts
	
L2
stop	B	stop

L100
	MOV R0, R8
	MOV R1, R9
	LDR R2, =BOARD
	MOV R3, R12
	BL computerMove	; compute move column for computer
	MOV R4, R0		; save move
	MOV R12, R0		; save previous move
	B L101


;
; your subroutines go here
;




;
; inithw subroutines
; performs hardware initialisation, including console
; parameters:
;	none
; return value:
;	none
;
inithw
	LDR	R0, =PINSEL0		; enable UART0 TxD and RxD signals
	MOV	R1, #0x50
	STRB	R1, [R0]
	LDR	R0, =U0LCR		; 7 data bits + parity
	LDR	R1, =0x02
	STRB	R1, [R0]
	BX	LR

;
; get subroutine
; returns the ASCII code of the next character read on the console
; parameters:
;	none
; return value:
;	R0 - ASCII code of the character read on teh console (byte)
;
get	LDR	R1, =U0LSR		; R1 -> U0LSR (Line Status Register)
get0	LDR	R0, [R1]		; wait until
	ANDS	R0, #0x01		; receiver data
	BEQ	get0			; ready
	LDR	R1, =U0RBR		; R1 -> U0RBR (Receiver Buffer Register)
	LDRB	R0, [R1]		; get received data
	BX	LR			; return

;
; put subroutine
; writes a character to the console
; parameters:
;	R0 - ASCII code of the character to write
; return value:
;	none
;
put	LDR	R1, =U0LSR		; R1 -> U0LSR (Line Status Register)
	LDRB	R1, [R1]		; wait until transmit
	ANDS	R1, R1, #0x20		; holding register
	BEQ	put			; empty
	LDR	R1, =U0THR		; R1 -> U0THR
	STRB	R0, [R1]		; output charcter
put0	LDR	R1, =U0LSR		; R1 -> U0LSR
	LDRB	R1, [R1]		; wait until
	ANDS	R1, R1, #0x40		; transmitter
	BEQ	put0			; empty (data flushed)
	BX	LR			; return

;
; puts subroutine
; writes the sequence of characters in a NULL-terminated string to the console
; parameters:
;	R0 - address of NULL-terminated ASCII string
; return value:
;	R0 - ASCII code of the character read on teh console (byte)
;
puts	STMFD	SP!, {R4, LR} 		; push R4 and LR
	MOV	R4, R0			; copy R0
puts0	LDRB	R0, [R4], #1		; get character + increment R4
	CMP	R0, #0			; 0?
	BEQ	puts1			; return
	BL	put			; put character
	B	puts0			; next character
puts1	LDMFD	SP!, {R4, PC} 		; pop R4 and PC

; disBoard subroutine
; displays the connect 4 board to the console
; parameters:
;	R0 - start address of board stored in memory
;	R1 - size of board
; return value:
;	nothing

disBoard	PUSH {R4-R10, lr}
	LDR R4, =0		; count = 0
	MOV R6, R1		; size
	MOV R5, R0		; currentAddress
	LDR R8, =0		; initilise char in row count
disBoard3	CMP R6, R4		; count >= size?
	BLE disBoard1
	LDRB R0, [R5], #1		; load char from board
	ADD R4, R4, #1	; increment count 
	ADD R8, R8, #1  ; increment char count of row 
	BL put
	CMP R8, #7	; if chars in row is 7 display new line
	BNE disBoard2
	LDR R0, =str_newl		;
	LDR R8, =0
	BL puts
disBoard2 B disBoard3
	
disBoard1 
	LDR R0, =str_help	; display instructions
	BL puts
	POP{R4-R10, pc}
	

; initBoard subroutine
; initilises a blank connect4 board and stores it in memory
; parameters:
;	R0 - start address in memory of space stored for board
;	R1 - size of board
; return vslue:
;	nothing

initBoard	PUSH{R4-R10, lr}

	MOV R4, R0		; current address in board
	MOV R6, R1		; size
	LDR R7, =0		; init count
initBoard2	CMP R6, R7		;
	BLS initBoard1
	LDR R5, =0x2D		; change char to '-'
	STRB R5, [R4], #1	
	ADD R7, R7, #1
	B initBoard2
	
initBoard1	POP{R4-R10, pc}

; checkMove Subroutine
; checks if a move on a given column is valid
; parameters:
;	R0 - column in which the move is wanted
;	R1 - start address of board 
; return values:
;	R0 - boolean for if move is valid

checkMove PUSH{R4-R10, lr}

	ADD R4, R0, R1	
	LDR R5, =COLS
checkMove3	LDRB R6, [R4], R5 ; load char from col in array
	CMP R6, #0x2D	; check if char is an empty space
	BNE checkMove2
	MOV R0, #1
	B checkMove4
checkMove2  MOV R0, #0
checkMove4 POP{R4-R10, pc}

; makeMove subroutine
; puts a piece down on the board
; parameters:
;	R0 - character of current player
;	R1 - column of which to put the piece
;	R2 - start address of board
; return values:
;	R0 - row where move was made

makeMove PUSH{R4-R10, lr}
	MOV R4, R0
	ADD R5, R1, R2
	LDR R6, =COLS
	LDR R9, =0	; initial row = 0
makeMove3	LDRB R7, [R5]	; currentRow	
	LDRB R8, [R5, R6]	; nextRow
	CMP R8, #0x2D	; compare next row to empty space
	BNE makeMove2	; if next row is not empty, place char in current row
	ADD R5, R5, R6	; add col
	ADD R9, R9, #1
	B makeMove3
	
makeMove2
	STRB R4, [R5]	; store char in board
	MOV R0, R9
	POP{R4-R10, pc}


; disMove subroutine
; displays whos move it is
; parameters:
;	R0 - character of current player
; return values;
;	R0 - 0 if player1, 1 if player2
disMove	PUSH{R4-R10, lr}
	MOV R4, R0
	BL put
	LDR R0, =str_turn	; display whos turn string
	BL puts
	CMP R4, #0x58		; check if move is 'X'
	BEQ disMove1
	MOV R0, #1
disMove2	POP{R4-R10, pc}
disMove1 MOV R0, #0
	B disMove2
	
	
	
; takeMove subroutine
; stores the move a player makes from the console
; parameters:
;	nothing
; return values:
;	R0 - column in which move was made

takeMove PUSH{R4-R10, lr}
takeMove2	BL get	;
	BL put	;
	MOV R4, R0
	LDR R6, =COLS	; check if char ascii inputted are between '1' and the number of columns
	ADD R5, R6, #0x30 
	CMP R4, #0x31
	BLT	takeMove1
	CMP R4, R5
	BGT takeMove1
	
	SUB R4, R0, #0x31	; returns the hexidecimal value of column input
	LDR R0, =str_newl	; display some new lines to increase readability
	BL puts
	LDR R0, =str_newl		; display new line for readability
	BL puts
	MOV R0, R4
	POP{R4-R10, pc}
	
takeMove1
	LDR R0, =str_tryAgain	; if move is invalid tell them to try again
	BL puts
	B takeMove2
	
	
	
; checkWin subroutine
; checks to see if the last move was a winning move
; parameters: 
;	R0 - current player
;	R1 - column of move
;	R2 - row of move
;	R3 - address of board
; return values
;	R0 - boolean if winning move or not

checkWin PUSH{R4-R11, lr}
	MOV R4, R0
	MOV R5, R1
	MOV R6, R2
	MOV R7, R3
	
	MOV R2, R7
	BL checkWinCol
	CMP R0, #1	; check if winner was returned
	BEQ checkWin1
	MOV R0, R4
	MOV R1, R6
	MOV R2, R7
	BL checkWinRow
	CMP R0, #1  ; check if winner was returned
	BEQ checkWin1
	
	MOV R0, R4
	MOV R1, R5
	MOV R2, R6
	MOV R3, R7
	BL checkWinDTop
	CMP R0, #1	; check if winner was returned
	BEQ checkWin1
	
	MOV R0, R4
	MOV R1, R5
	MOV R2, R6
	MOV R3, R7
	BL checkWinDBot


checkWin1 POP{R4-R11, pc}



; checkWinCol subroutine
; checks to see if a given column has four in a row
; parameters:
;	R0 - currentPlayer
;	R1 - column of move
;	R2 - address of board
; return values:
;	R0 - boolean if winning move

checkWinCol
	PUSH{R4-R10, lr}
	ADD R4, R1, R2
	MOV R5, R0
	LDR R0, =0	; isWinner = false
	LDR R6, =0	;count = 0
	LDR R7, =COLS
	LDR R9, = SIZE
	ADD R10, R2, R9	; end postion of board
checkWinCol3	
	CMP R4, R10	; checks if current char is outside of board
	BGT checkWinCol1
	LDRB R8, [R4], R7	; loads char from board and offsets address by number of columns
	CMP R8, R5			; checks if char is same as current player
	BEQ checkWinCol2
	MOV R6, #0			; if not reset count
	B checkWinCol3

checkWinCol2
	ADD R6, R6, #1	; increment count
	CMP R6, #4	; checks if count is at least 4 and returns winner if so
	BGE checkWinCol4
	B checkWinCol3

checkWinCol4
	MOV R0, #1
checkWinCol1 
	POP{R4-R10, pc}

	
; checkWinRow subroutine
; checks to see if a move won on a given row
; parameters:
;	R0 - currentPlayer
;	R1 - row of move
;	R2 - address of board
; return values
;	R0 - boolean if winning move or not
checkWinRow
	PUSH{R4-R10, lr}
	LDR R4, =COLS
	MUL R5, R1, R4
	ADD R5, R5, R2	; change current char to first char in row
	ADD R6, R5, R4	; change end address to next row
	LDR R7, =0		; count = 0
checkWinRow4
	CMP R5, R6		; compare current char to next row
	BGE	checkWinRow2
	LDRB R8, [R5], #1	; load char and increment address
	CMP R0, R8			; compare char to current player
	BEQ checkWinRow3
	MOV R7, #0	; reset count
	B checkWinRow4

checkWinRow3
	ADD R7, R7, #1	; increment count
	CMP R7, #4		; compare count to 4 and return winner if true
	BNE checkWinRow4
	MOV R0, #1
checkWinRow1
	POP{R4-R10, pc}
	
checkWinRow2
	MOV R0, #0
	B checkWinRow1
	
; disWinner subroutine
; displays a winner if one exists
; parameters:
;	R0 - winning player
; return values:
;	nothing

disWinner PUSH{R4-R10, lr}
	MOV R4, R0
	LDR R0, =str_win
	BL puts
	MOV R0, R4
	BL put
	POP {R4-R10, pc}

; checkWinDTop subroutine
; checks if a move was a winner in the top left diagonal
; parameters:
;	R0 - current player
;	R1 - column of move
;	R2 - row of move
;	R3 - address of board
; return values:
;	R0 - boolean if winning move or not

checkWinDTop PUSH{R4-R11, lr}
	MOV R4, R0;
	MOV R5, R1; col
	MOV R6, R2; row
	MOV R7, R3;
	MOV R0, #0
	LDR R11, = 0 ; count
	
checkWinDTop3
	CMP R5, #0
	BEQ checkWinDTop2		; keeps removing one from the row and column till one is at 0
	CMP R6, #0
	BEQ checkWinDTop2
	SUB R5, R5, #1
	SUB R6, R6, #1
	B checkWinDTop3
	
checkWinDTop2
	ADD R8, R7, R5		; calculates the position of the current char in the board
	LDR R10, =COLS
	MUL R9, R6, R10
	ADD R8, R8, R9
	LDRB R9, [R8]
	CMP R9, R4		; checks if char in board is same as persons move
	BEQ checkWinDTop4
	MOV R11, #0	; reset count
	B checkWinDTop5
	
checkWinDTop4
	ADD R11, R11, #1	; increase count by 1
	CMP R11, #4 		; if count is at lease 4 return winner
	BEQ checkWinDTop6
	B checkWinDTop5
	
checkWinDTop5		; increments row and column until reaches end of board
	CMP R5, #6
	BEQ checkWinDTop1
	CMP R6, #5
	BEQ checkWinDTop1
	ADD R5, R5, #1
	ADD R6, R6, #1
	B checkWinDTop2
	

checkWinDTop6
	MOV R0, #1

checkWinDTop1 POP{R4-R11, pc}

; checkWinDBot subroutine
; checks if a move was a winner in the bottom left diagonal
; parameters:
;	R0 - current player
;	R1 - column of move
;	R2 - row of move
;	R3 - address of board
; return values:
;	R0 - boolean if winning move or not

checkWinDBot PUSH{R4-R10, lr}
	MOV R4, R0;
	MOV R5, R1; col
	MOV R6, R2; row
	MOV R7, R3;
	MOV R0, #0
	LDR R11, = 0 ; count
	
checkWinDBot3
	CMP R5, #0		; removes 1 from column and adds 1 to row until row and column is in bottom left position
	BEQ checkWinDBot2
	CMP R6, #5
	BEQ checkWinDBot2
	SUB R5, R5, #1
	ADD R6, R6, #1
	B checkWinDBot3
	
checkWinDBot2
	ADD R8, R7, R5	; caluclates the char address in board using the row and column
	LDR R10, =COLS
	MUL R9, R6, R10
	ADD R8, R8, R9
	LDRB R9, [R8]
	CMP R9, R4 ; checks if char is same as current player
	BEQ checkWinDBot4
	MOV R11, #0	; reset count
	B checkWinDBot5
	
checkWinDBot4
	ADD R11, R11, #1	; increments count
	CMP R11, #4	; checks if count is at least 4 and is so returns winner
	BEQ checkWinDBot6
	B checkWinDBot5
	
checkWinDBot5
	CMP R5, #6	; increases column and decreases row until current char is in the top right most position
	BEQ checkWinDBot1
	CMP R6, #0
	BEQ checkWinDBot1
	ADD R5, R5, #1
	SUB R6, R6, #1
	B checkWinDBot2
	

checkWinDBot6
	MOV R0, #1



checkWinDBot1 POP{R4-R10, pc}

; computerMove subroutine
; replaces the other player with the computer and generates it's next move
; parameters:
;	R0 - char of player1
;	R1 - char of computer
;	R2 - start address of board
;	R3 - previous move
; return values:
;	R0 - column of computer move

computerMove PUSH{R4-R10, lr}
	MOV R4, R0 ; char of player1 piece
	MOV R5, R1 ; char of computer piece
	MOV R6, R2 ; start address of board
	MOV R10, R3	; previous move
	LDR R7, =0 ; current col = 0
	LDR R8, =COLS ; end of cols
computerMove5
	CMP R7, R8	; current col at end?
	BGE computerMove2
	MOV R0, R7	; col
	MOV R1, R6	; board address
	BL checkMove
	CMP R0, #1
	BNE computerMove3
	MOV R0, R5 	; computer piece
	MOV R1, R7	; col
	MOV R2, R6	; adress of board
	BL makeMove
	
	MOV R9, R0	; row
	;MOV R0, R6
	;LDR R1, =SIZE
	;BL disBoard
	MOV R2, R9	; row
	MOV R0, R5	; computer piece
	MOV R1, R7	; col
	MOV R3, R6 ; address of board
	BL checkWin
	CMP R0, #1
	BEQ computerMove4
	MOV R0, R7	; row
	MOV R1, R9	; col
	MOV R2, R6	; board address
	BL clearMove
	
computerMove3	
	;MOV R0, R6
	;LDR R1, =SIZE
	;BL disBoard
	ADD R7, R7, #1
	B computerMove5


computerMove2
	LDR R7, =0 ; current col = 0
	LDR R8, =COLS ; end of cols
computerMove15
	CMP R7, R8	; current col at end?
	BGE computerMove12
	MOV R0, R7	; col
	MOV R1, R6	; board address
	BL checkMove
	CMP R0, #1
	BNE computerMove13
	MOV R0, R4 	; player piece
	MOV R1, R7	; col
	MOV R2, R6	; adress of board
	BL makeMove
	MOV R9, R0	; row
	;MOV R0, R6
	;LDR R1, =SIZE
	;BL disBoard
	MOV R2, R9	; row
	MOV R0, R4	; player piece
	MOV R1, R7	; col
	MOV R3, R6 ; address of board
	BL checkWin
	CMP R0, #1
	BEQ computerMove4

	MOV R0, R7	; row
	MOV R1, R9	; col
	MOV R2, R6	; board address
	BL clearMove
computerMove13	
	;MOV R0, R6
	;LDR R1, =SIZE
	;BL disBoard

	ADD R7, R7, #1
	B computerMove15
	
computerMove12
	CMP R10, #COLS
	BEQ computerMove20
	ADD R10, R10, #1
computerMove21
	MOV R0, R10	; col
	MOV R1, R6	; board address
	BL checkMove
	CMP R0, #1	; move valid?
	BNE computerMove12
	MOV R0, R10
	B computerMove1
	
computerMove20
	MOV R10, #0
	B computerMove21
	

computerMove4 
	MOV R0, R7	; row
	MOV R1, R9	; col
	MOV R2, R6	; board address
	BL clearMove
	MOV R0, R7	; col
computerMove1 POP{R4-R10, pc}


; clearMove subroutine
; removes a piece from the board and replaces it with a blank '-'
; parameters:
;	R0 - col of piece
; 	R1 - row of piece
;	R2 - start address of board
; return values:
;	nothing

clearMove PUSH{R4-R10, lr}
	LDR R4, =COLS ; number of cols
	MUL R4, R1, R4	; find row address of piece
	ADD R4, R4, R0	; find exact address of piece
	ADD R4, R4, R2	; add this to address of board
	LDR R5, =0x2D	; '-' char
	STRB R5, [R4]	; stores '-' at address of piece
	
	POP{R4-R10, pc}
	
; randomCol subroutine
; returns a random column
; parameters:
;	none
; return values:
;	R0 - random col
randomMove PUSH{R4-R10, lr}
	
	
	
	
	POP{R4-R10, pc}
;
; hint! put the strings used by your program here ...
;



str_go
	DCB	"Let's play Connect4!!",0xA, 0xD, 0xA, 0xD, 0

str_newl
	DCB	0xA, 0xD, 0x0
	
str_turn
	DCB " please make your move (1-7) : ", 0xA, 0xD, 0xA, 0xD, 0 
	
str_tryAgain
	DCB " invalid input please try again: ", 0xA, 0xD, 0xA, 0xD, 0 
	
str_win
	DCB "Congratulation, you have won player ", 0
	
str_help
	DCB "1234567",  0xA, 0xD, 0xA, 0xD, 0 

str_draw
	DCB "Game Over, it's a draw!",  0xA, 0xD, 0xA, 0xD, 0 

str_game
	DCB "Please press 1 for another player (2 for computer)> ",  0xA, 0xD, 0xA, 0xD, 0 

	END
