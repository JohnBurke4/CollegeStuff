library ieee;
use ieee.std_logic_1164.all;

entity shifter_16bit is
    port( 
		s0, s1 : in  std_logic;
        input : in  std_logic_vector (15 downto 0);
		output : out  std_logic_vector (15 downto 0)
	);
end shifter_16bit;

architecture behavioral of shifter_16bit is

constant prop_delay: time := 1 ns;

begin

output(0) <= 
		input(1) after prop_delay when s1 = '0' and s0 ='1' else
		'0' after prop_delay when s1 = '1' and s0 = '0' else 
		input(0) after prop_delay;
		
output(1) <= 
		input(2) after prop_delay when s1 = '0' and s0 = '1' else
		input(0) after prop_delay when s1 = '1' and s0 = '0' else 
		input(1) after prop_delay;
		
output(2) <= 
		input(3) after prop_delay when s1 = '0' and s0 = '1' else
		input(1) after prop_delay when s1 = '1' and s0 = '0' else 
		input(2) after prop_delay;
		
output(3) <= 
		input(4) after prop_delay when s1 = '0' and s0 = '1' else
		input(2) after prop_delay when s1 = '1' and s0 = '0' else 
		input(3) after prop_delay;
		
output(4) <=
		input(5) after prop_delay when s1 = '0' and s0 = '1' else
		input(3) after prop_delay when s1 = '1' and s0 = '0' else 
		input(4) after prop_delay;
		
output(5) <=
		input(6) after prop_delay when s1 = '0' and s0 = '1' else
		input(4) after prop_delay when s1 = '1' and s0 = '0' else 
		input(5) after prop_delay;
		
output(6) <= 
		input(7) after prop_delay when s1 = '0' and s0 = '1' else
		input(5) after prop_delay when s1 = '1' and s0 = '0' else 
		input(6) after prop_delay;
		
output(7) <= 
		input(8) after prop_delay when s1 = '0' and s0 = '1' else
		input(6) after prop_delay when s1 = '1' and s0 = '0' else 
		input(7) after prop_delay;
		
output(8) <= 
		input(9) after prop_delay when s1 = '0' and s0 = '1' else
		input(7) after prop_delay when s1 = '1' and s0 = '0' else 
		input(8) after prop_delay;
		
output(9) <= 
		input(10) after prop_delay when s1 = '0' and s0 = '1' else
		input(8) after prop_delay when s1 = '1' and s0 = '0' else 
		input(9) after prop_delay;
		
output(10) <= 
		input(11) after prop_delay when s1 = '0' and s0 = '1' else
		input(9) after prop_delay when s1 = '1' and s0 = '0' else 
		input(10) after prop_delay;
		
output(11) <= 
		input(12) after prop_delay when s1 = '0' and s0 = '1' else
		input(10) after prop_delay when s1 = '1' and s0 = '0' else 
		input(11) after prop_delay;
		
output(12) <= 
		input(13) after prop_delay when s1 = '0' and s0 = '1' else
		input(11) after prop_delay when s1 = '1' and s0 = '0' else 
		input(12) after prop_delay;
		
output(13) <=
		input(14) after prop_delay when s1 = '0' and s0 = '1' else
		input(12) after prop_delay when s1 = '1' and s0 = '0' else 
		input(13) after prop_delay;
		
output(14) <= 
		input(15) after prop_delay when s1 = '0' and s0 = '1' else
		input(13) after prop_delay when s1 = '1' and s0 = '0' else 
		input(14) after prop_delay;
		
output(15) <= 
		'0' after prop_delay when s1 = '0' and s0 = '1' else
		input(14) after prop_delay when s1 = '1' and s0 = '0' else 
		input(15) after prop_delay;
		
end behavioral;
