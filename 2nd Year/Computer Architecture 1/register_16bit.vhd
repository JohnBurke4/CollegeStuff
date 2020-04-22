library ieee;
use ieee.std_logic_1164.all;

entity register_16bit is
	port(
		input: in std_logic_vector(15 downto 0);
		load, clk : in std_logic;
		output: out std_logic_vector(15 downto 0)
	);
end register_16bit;

architecture behavioral of register_16bit is

signal current_value : std_logic_vector(15 downto 0) := "0000000000000000";
constant prop_delay : time := 1 ns;

begin
	process(clk, load, input)
	begin
		if (rising_edge(clk)) then
			if load='1' then
				current_value <= input;
				output <= input after prop_delay;
			elsif load = '0' then
				output <= current_value after prop_delay;
			end if;
		end if;
	end process;
end behavioral;