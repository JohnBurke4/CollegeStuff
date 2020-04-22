library ieee;
use ieee.std_logic_1164.all;

entity full_adder is
	port(
		x, y, carry_in : in std_logic;
		output : out std_logic;
		carry_out : out std_logic
	);
end full_adder;

architecture behavioral of full_adder is

constant prop_delay : time := 1 ns;

begin

	output <= x xor y xor carry_in after prop_delay;
	carry_out <= ( x and y ) or ( carry_in and ( x xor y ) ) after prop_delay;

end behavioral;

