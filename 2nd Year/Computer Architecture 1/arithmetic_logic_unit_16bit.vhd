library ieee;
use ieee.std_logic_1164.all;

entity arithmetic_logic_unit_16bit is
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
end arithmetic_logic_unit_16bit;

architecture behavioral of arithmetic_logic_unit_16bit is

component multiplexor_2_16bit
    port(
         s : in  std_logic;
         in0 : in  std_logic_vector(15 downto 0);
         in1 : in  std_logic_vector(15 downto 0);
         output : out  std_logic_vector(15 downto 0)
    );
end component;

component arithmetic_unit_16bit
port(
	s : in std_logic_vector(1 downto 0); 
	carry_in : in  std_logic;
    input_a : in  std_logic_vector(15 downto 0);
    input_b : in  std_logic_vector(15 downto 0);
	output : out  std_logic_vector(15 downto 0);
	carry_out : out  std_logic
);
end component;

component logic_unit_16bit
	port( 
		s : in std_logic_vector(1 downto 0);
		input_a : in  std_logic_vector(15 downto 0);
		input_b : in  std_logic_vector(15 downto 0);
		output : out  std_logic_vector(15 downto 0)
	);
end component;


signal arithmetic_unit_out : std_logic_vector(15 downto 0);
signal logic_unit_out : std_logic_vector(15 downto 0);
signal result : std_logic_vector(15 downto 0);
signal carry_result : std_logic;

constant prop_delay : time := 1 ns;

begin
 
multiplexor_select: multiplexor_2_16bit port map (
	s => s(2),
	in0 => arithmetic_unit_out,
	in1 => logic_unit_out,
	output => result
);
 
arithmetic_unit: arithmetic_unit_16bit port map(
	s => s(1 downto 0),
	carry_in => carry_in,
	input_a => input_a,
	input_b => input_b,
	output => arithmetic_unit_out,
	carry_out => carry_result
);
 
logic_unit: logic_unit_16bit port map(
	s => s(1 downto 0),
	input_a => input_a,
	input_b => input_b,
	output => logic_unit_out
);
 
output <= result;
carry <= carry_result;
overflow <= carry_result;

negative <= 
	'1' after prop_delay when result(15) = '1' else
	'0' after prop_delay;
		
zero <= 
	'1' after prop_delay when result = "0000000000000000" else
	'0' after prop_delay;
 
end behavioral;