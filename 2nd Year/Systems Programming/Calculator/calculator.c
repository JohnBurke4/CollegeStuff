#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <math.h>
#include <stdbool.h>
#include <string.h>

struct double_stack {
	double * items;
	int max_size;
	int top;	
};

struct double_stack * double_stack_new(int max_size);
void double_stack_push(struct double_stack * this, double value);
double double_stack_pop(struct double_stack * this);
bool double_stack_empty(struct double_stack * this);
double double_stack_peek(struct double_stack * this);
double evaluate_postfix_expression(char ** expression, int size);
double evaluate_infix_expression(char ** expression, int size);

struct double_stack * double_stack_new(int max_size) {
	struct double_stack * result;

	result = malloc(sizeof(struct double_stack));
	result -> max_size = max_size;
	result -> top = 0;
	result -> items = malloc(sizeof(double)*max_size);
	return result;
}

int main(int argc, char ** argv) {
	if ( argc == 1 ) {
		// command line contains only the name of the program
		printf("Error: No command line parameters provided\n");
		printf("Usage: %s postfix|infix <expression>\n", argv[0]);
		exit(1);
	}
	else if ( argc == 2 ) {
                // command line contains name of prog and one other parameter
		printf("Error: No expression to evaluate provided\n");
		printf("Usage: %s postfix|infix <expression>\n", argv[0]);
		exit(1);
		}
	else {
		// command line has enough parameters for an expression
		double result;
		if ( strcmp(argv[1], "postfix") == 0 ) {
			// pass the command line parameters, but with the first two removed
		      	result = evaluate_postfix_expression(argv+2, argc-2);
		     	printf("Result is %lf\n", result);
		}
		else if ( strcmp(argv[1], "infix") == 0 ) {
		 	// pass the command line parameters, but with the first two removed
		      	result = evaluate_infix_expression(argv+2, argc-2);
		     	printf("Result is %lf\n", result);
		}
		else {
		        printf("Error: You must specify whether the expression is infix or postfix\n");
		       	printf("Usage: %s postfix|infix <expression>\n", argv[0]);
		        exit(1);
		}
		return(0);
	}
}

void double_stack_push(struct double_stack * this, double value) {
	int index = this -> top;
	this -> items[index] = value;
	this -> top += 1;
}

double double_stack_pop(struct double_stack * this) {
	this -> top -= 1;
	int index = this -> top;
	double value = this -> items[index];
	return value;
}

double double_stack_peek(struct double_stack * this) {
	int index = this -> top - 1;
	double value = this -> items[index];
	return value;
}

bool double_stack_empty(struct double_stack * this) {
	return this -> top == 0;
}

int verifySymbol(char * symbol){
	if (strcmp(symbol, "-") == 0){
		return 1;
	}
	else if (strcmp(symbol, "+") == 0){
		return 2;
	}
	else if (strcmp(symbol, "/") == 0){
		return 3;
	}
	else if (strcmp(symbol, "X") == 0){
	 	return 4;      	
	}
	else if (strcmp(symbol, "^") == 0){
		return 5;
	}
	else if (strcmp(symbol, "(") == 0){
		return 6;
	}
	else if (strcmp(symbol, ")") == 0){
		return 7;
	}
	return 0;
}

double evaluate_postfix_expression(char ** expression, int size){
	struct double_stack * numbers = double_stack_new(size);  	
	for (int i = 0; i < size; i++) {
		if (verifySymbol(expression[i]) != 0){
			double num1 = double_stack_pop(numbers);
			double num2 = double_stack_pop(numbers);
			double result;
			switch(verifySymbol(expression[i])){
				case 1: result = num2 - num1;
					break;
				case 2: result = num2 + num1;
					break;
				case 3: result = num2 / num1;
					break;
				case 4: result = num2 * num1;
					break;
				case 5: result = pow(num2, num1);
					break;
			}
			double_stack_push(numbers, result);
		}
		else {
			double d;
			sscanf(expression[i], "%lf", &d);
			double_stack_push(numbers, d);
		}
	}
	return double_stack_pop(numbers);
}

double evaluate_infix_expression(char ** expression, int size){
	struct double_stack * operatorIndex = double_stack_new(size);
	char ** result = malloc(sizeof(char *)* size);
      	int indexResult = 0;	
	for (int i = 0; i < size; i++){
		char * symbol = expression[i];
		switch(verifySymbol(symbol)){
			case 0: result[indexResult] = symbol;
				indexResult++;
				break;
			case 1:	
			case 2:
			case 3:
			case 4:
			case 5:
				if (!double_stack_empty(operatorIndex)) {
					char * stackTop = expression[(int)double_stack_peek(operatorIndex)];
					int order1 = verifySymbol(symbol);
					int order2 = verifySymbol(stackTop);
					while (order2 > order1 && !double_stack_empty(operatorIndex) && order2 < 6) {
						result[indexResult] = stackTop;
						double_stack_pop(operatorIndex);
						indexResult++;
						stackTop = expression[(int)double_stack_peek(operatorIndex)];
						order2 = verifySymbol(stackTop);
					}
				}	
				double_stack_push(operatorIndex, i);
				break;
			case 6: double_stack_push(operatorIndex, i);
				break;
			case 7: ;	
				int order = verifySymbol(expression[(int)double_stack_peek(operatorIndex)]);
				while (!double_stack_empty(operatorIndex) && order !=6){
					result[indexResult] = expression[(int)double_stack_pop(operatorIndex)];
					indexResult++;
					order = verifySymbol(expression[(int)double_stack_peek(operatorIndex)]);
				}
				double_stack_pop(operatorIndex);
		}
	}
	while (!double_stack_empty(operatorIndex)){
		char * operator = expression[(int)double_stack_pop(operatorIndex)];
		result[indexResult] = operator;
		indexResult++;
	}
	
	/*for (int i = 0; i < indexResult; i++){
		printf("%s", result[i]);
	}*/
	return evaluate_postfix_expression(result, indexResult);
}

