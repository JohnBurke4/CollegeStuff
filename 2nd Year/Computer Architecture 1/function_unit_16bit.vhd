library ieee;
use ieee.std_logic_1164.all;

entity function_unit_16bit is
    port ( 
		s : in std_logic_vector(4 downto 0);
		V, C, N, Z : out  std_logic;
        input_a : in  std_logic_vector (15 downto 0);
		input_b : in  std_logic_vector (15 downto 0);
		output : out  std_logic_vector (15 downto 0)
	);
end function_unit_16bit;

architecture behavioral of function_unit_16bit is

	component multiplexor_2_16bit
    port(
         s : in std_logic;
         in0 : in std_logic_vector(15 downto 0);
         in1 : in std_logic_vector(15 downto 0);
         output : out std_logic_vector(15 downto 0)
    );
	end component;
	
	component arithmetic_logic_unit_16bit
	port( 
		s : in std_logic_vector(2 downto 0); 
		carry_in : in  std_logic;
		input_a : in  std_logic_vector(15 downto 0);
		input_b : in  std_logic_vector(15 downto 0);
		carry : out std_logic;
		negative : out std_logic;
		overflow : out std_logic;
		zero : out std_logic;
		output : out  std_logic_vector(15 downto 0)
	);
	end component;

	component shifter_16bit
	port(
		s0, s1 : in  std_logic;
		input : in  std_logic_vector (15 downto 0);
		output : out  std_logic_vector (15 downto 0)
	);
  	end component;


	signal arithmetic_logic_unit_out : std_logic_vector(15 downto 0);
	signal shifter_out : std_logic_vector(15 downto 0);


begin

 	multiplexor_select: multiplexor_2_16bit port map (
		s => s(4),
		in0 => arithmetic_logic_unit_out,
		in1 => shifter_out,
		output => output
 	);
 
 	arithmetic_logic_unit : arithmetic_logic_unit_16bit port map (
		carry_in => s(0),
		s => s(3 downto 1),
		input_a => input_a,
		input_b => input_b,
		carry => C,
		negative => N,
		overflow => V,
		zero => Z,
		output => arithmetic_logic_unit_out
	);
	
	shifter : shifter_16bit port map (
		s0 => s(2), 
		s1 => s(3), 
		input => input_b, 
		output => shifter_out
	);
	
end behavioral;
