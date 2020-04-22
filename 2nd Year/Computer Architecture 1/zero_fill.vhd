library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_arith.all;
use ieee.std_logic_unsigned.all;

entity zero_fill is
port( 
	input: in std_logic_vector(2 downto 0);
	output : out std_logic_vector( 15 downto 0)
	);	
end zero_fill;

architecture behavioral of zero_fill is

constant prop_delay : time := 1 ns;	

begin
	
	output(15 downto 3) <=  "0000000000000" after prop_delay;
	output(2 downto 0) <= input after prop_delay;
	
end behavioral;