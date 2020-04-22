library ieee;
use ieee.std_logic_1164.all;

entity multiplexor_4_1bit is
    port ( 
			s0, s1 : in  std_logic;
			in0 : in  std_logic;
           	in1 : in  std_logic;
		   	in2 : in  std_logic;
		   	in3 : in  std_logic;
			output : out  std_logic
	);
end multiplexor_4_1bit;

architecture behavioral of multiplexor_4_1bit is

constant prop_delay : time := 1 ns;

begin
	 output <= 	in0 after prop_delay when s1='0' and s0='0' else
			in1 after prop_delay when s1='0' and s0='1' else
			in2 after prop_delay when s1='1' and s0='0' else
			in3 after prop_delay when s1='1' and s0='1' else
			'0' after prop_delay;
end behavioral;