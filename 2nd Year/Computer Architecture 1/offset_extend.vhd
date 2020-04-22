library ieee;
use ieee.std_logic_1164.all;

entity offset_extend is
port( 
	dr : in std_logic_vector(2 downto 0);
	sb : in std_logic_vector(2 downto 0);
	output : out std_logic_vector( 15 downto 0)
	);	
end offset_extend;

architecture behavioral of offset_extend is

constant prop_delay : time := 1 ns;

begin

	output(15 downto 6) <= 
		"1111111111" after prop_delay when dr(2) ='1' else
	 	"0000000000" after prop_delay when dr(2) ='0';
	output(5 downto 3) <= dr after prop_delay;
	output(2 downto 0) <= sb after prop_delay;
	 
end behavioral;