library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_arith.all;
use ieee.std_logic_unsigned.all;

entity program_counter is
	port( 
		pl : in std_logic;
		pi: in std_logic;
		clk: in std_logic;
		offset : in std_logic_vector(15 downto 0);
		output : out std_logic_vector(15 downto 0)
	);
end program_counter;

architecture behavioral of program_counter is

begin
	process(clk)
	variable current_address: integer := 0; 
	begin
		if (rising_edge(clk)) then
			if pl='1' then
				current_address := current_address + conv_integer(offset);
				output <= conv_std_logic_vector(current_address, output'length);
			elsif pi='1' then 
				current_address := current_address + 1;
				output <= conv_std_logic_vector(current_address, output'length);
			else
				output <= conv_std_logic_vector(current_address, output'length);
			end if;
		end if;
	end process;
end behavioral;
