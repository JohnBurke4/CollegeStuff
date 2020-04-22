library ieee;
use ieee.std_logic_1164.all;

entity function_unit_16bit_tb is
end function_unit_16bit_tb;

architecture behavioral of function_unit_16bit_tb is

-- Component Declaration for the Unit Under Test (UUT)

component function_unit_16bit
port( 
	s : in std_logic_vector(4 downto 0);
	V, C, N, Z : out  std_logic;
    input_a : in  std_logic_vector (15 downto 0);
	input_b : in  std_logic_vector (15 downto 0);
    output : out  std_logic_vector (15 downto 0)
);
end component;

--Inputs
signal s : std_logic_vector(4 downto 0);
signal input_a : std_logic_vector(15 downto 0);
signal input_b : std_logic_vector(15 downto 0);

--Outputs
signal C : std_logic;
signal V : std_logic;
signal N : std_logic;
signal Z : std_logic;
signal output : std_logic_vector(15 downto 0);

--Clock
constant clk_period : time := 20 ns;

begin

-- Instantiate the Unit Under Test (UUT)
uut: function_unit_16bit port map(
	s => s,
	C => C,
	V => V,
	Z => Z,
	N => N,
	input_a => input_a,
	input_b => input_b,
	output => output
);
 
 
stim_proc: process
begin		

    input_a <= "1111111100000000";
	input_b <= "0000000011111111";
	s <= "00000";
	
    wait for clk_period;	
     
	s <= "00001";
	
    wait for clk_period;	
     
	s <= "00010";
	
    wait for clk_period;	
     
	s <= "00011";
	
    wait for clk_period;	
     
    s <= "00100";
	
    wait for clk_period;	
     
    s <= "00101";
	
    wait for clk_period;	
     
    s <= "00110";
	
    wait for clk_period;	
     
    s <= "00111";
	
    wait for clk_period;	
     
	s <= "01000";
	
    wait for clk_period;	
     
	s <= "01010";
	
    wait for clk_period;	
     
    s <= "01100";
	
    wait for clk_period;	
     
    s <= "01110";
	
    wait for clk_period;	
     
	s <= "10000";
	
    wait for clk_period;	
      
	s <= "10100";
	
    wait for clk_period;	
     
    s <= "11000";
	
    wait for clk_period;
     
end process;
  
end;