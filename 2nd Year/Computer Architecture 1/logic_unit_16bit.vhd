library ieee;
use ieee.std_logic_1164.all;

entity logic_unit_16bit is
    port( 
		s : in std_logic_vector(1 downto 0);
        input_a : in  std_logic_vector(15 downto 0);
        input_b : in  std_logic_vector(15 downto 0);
		output : out  std_logic_vector(15 downto 0)
	);
end logic_unit_16bit;

architecture behavioral of logic_unit_16bit is

component multiplexor_4_16bit
	port(
	   s0, s1 : in  std_logic;
	   in0 : in  std_logic_vector(15 downto 0);
	   in1 : in  std_logic_vector(15 downto 0);
	   in2 : in  std_logic_vector(15 downto 0);
	   in3 : in  std_logic_vector(15 downto 0);
	   output : out  std_logic_vector(15 downto 0)
	);
end component;
 
begin

 	multiplexor_logic_select: multiplexor_4_16bit port map(
		s0 => s(0),
		s1 => s(1),
		in0 => ( input_a and input_b ),
		in1 => ( input_a or input_b ),
		in2 => ( input_a xor input_b ),
		in3 => ( not input_a ),
		output => output
	);	
	
end behavioral;
