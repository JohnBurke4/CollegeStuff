library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_arith.all;
use ieee.std_logic_unsigned.all;

entity control_address_register is
	port( 
			address : in std_logic_vector(7 downto 0);
			load: in std_logic;
			clk : in std_logic;
			output : out std_logic_vector(7 downto 0)
	);
end control_address_register;

architecture Behavioral of control_address_register is

begin
	process(clk, load)
		variable current_address : integer := 191;
		begin
			if (rising_edge(clk)) then
				if load='1' then
					output <= address after 1ns;
					current_address := conv_integer(address);
				else 
					current_address := current_address + 1;
					output <= conv_std_logic_vector(current_address, output'length);
				end if;
			end if;
	end process;
end Behavioral;
