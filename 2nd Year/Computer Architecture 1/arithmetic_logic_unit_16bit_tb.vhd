--------------------------------------------------------------------------------
library ieee;
use ieee.std_logic_1164.all;
 
entity arithmetic_logic_unit_16bit_tb is
end arithmetic_logic_unit_16bit_tb;
 
architecture behavior of arithmetic_logic_unit_16bit_tb is 
 
-- Component Declaration for the Unit Under Test (UUT)
 
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
    

--Inputs
signal s : std_logic_vector(2 downto 0);
signal carry_in : std_logic;
signal input_a : std_logic_vector(15 downto 0);
signal input_b : std_logic_vector(15 downto 0);
   
--Outputs
signal carry : std_logic;
signal negative : std_logic;
signal overflow : std_logic;
signal zero : std_logic;
signal output : std_logic_vector(15 downto 0);
  
--Clock
constant clk_period : time := 15ns;

begin
 
-- Instantiate the Unit Under Test (UUT)
uut: arithmetic_logic_unit_16bit port map (
          s => s,
          carry_in => carry_in,
          input_a => input_a,
          input_b => input_b,
		  carry => carry,
		  overflow => overflow,
		  zero => zero,
		  negative => negative,
          output => output
);

stim_proc: process
begin	

    input_a <= "1111111100000000";
	input_b <= "0000000011111111";
	carry_in <= '0';
	s <= "000";
	
    wait for clk_period;	
     
    carry_in <= '1';
	s <= "000";
	
    wait for clk_period;	
     
    carry_in <= '0';
	s <= "001";
	
    wait for clk_period;	
     
    carry_in <= '1';
	s <= "001";
	
    wait for clk_period;	
     
    carry_in <= '0';
	s <= "010";
	
    wait for clk_period;	
     
    carry_in <= '1';
	s <= "010";
	
    wait for clk_period;	
     
    carry_in <= '0';
	s <= "011";
	
    wait for clk_period;	
     
    carry_in <= '1';
	s <= "011";
	
    wait for clk_period;	
     
    carry_in <= '0';
	s <= "100";
	
    wait for clk_period;	
     
    carry_in <= '1';
	s <= "100";

    wait for clk_period;	
     
    carry_in <= '0';
	s <= "101";

    wait for clk_period;	
     
    carry_in <= '1';
	s <= "101";
	
    wait for clk_period;	
     
    carry_in <= '0';
	s <= "110";
	
    wait for clk_period;	
      
    carry_in <= '1';
	s <= "110";
	
    wait for clk_period;	
     
    carry_in <= '0';
	s <= "111";
	
    wait for clk_period;
     
    carry_in <= '1';
	s <= "111";
	
    wait for clk_period;	
     
end process;

end;
