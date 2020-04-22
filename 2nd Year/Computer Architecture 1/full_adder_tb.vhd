library ieee;
use ieee.std_logic_1164.all;
 
entity full_adder_tb is
end full_adder_tb;

architecture behavioral of full_adder_tb is

--Component Declaration for the Unit Under Test (UUT)

component full_adder
port(
	x, y, carry_in : in std_logic;
	output : out std_logic;
	carry_out : out std_logic
);
end component;

--Inputs
signal x : std_logic;
signal y : std_logic;
signal carry_in : std_logic;

--Inputs
signal output : std_logic;
signal carry_out : std_logic;

--Clock
constant clk_period : time := 5 ns;
   
begin   

-- Instantiate the Unit Under Test (UUT)
	uut: full_adder port map (
          x => x,
          y => y,
          carry_in => carry_in,
          carry_out => carry_out,
		  output => output
        );
		
	stim_proc: process
   	begin

   		x <= '0';
	  	y <= '0';
	  	carry_in <= '0';
	  
		wait for 5 ns;	
		  
      	x <= '1';
	  	y <= '0';
	  	carry_in <= '0';
	  
		wait for 5 ns;
		  	
		x <= '0';
	  	y <= '1';
	 	carry_in <= '0';
	  
		wait for 5 ns;
		  	
      	x <= '1';
	  	y <= '1';
	  	carry_in <= '0';
	  
	  
		wait for 5 ns;	
		  
		x <= '0';
	  	y <= '0';
	  	carry_in <= '1';
		
		wait for 5 ns;

		x <= '1';
	  	y <= '0';
	  	carry_in <= '1';
	
		wait for 5 ns;

		x <= '0';
	  	y <= '1';
	  	carry_in <= '1';
	  
		wait for 5 ns;
		  
		x <= '1';
	  	y <= '1';
	  	carry_in <= '1';
	  
	  	wait for 5 ns;
   
   end process;
   
end;