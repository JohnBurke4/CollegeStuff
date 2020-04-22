library ieee;
use ieee.std_logic_1164.all;
 
entity ripple_adder_16bit_tb is
end ripple_adder_16bit_tb;

architecture behavioral of ripple_adder_16bit_tb is

--Component Declaration for the Unit Under Test (UUT)

component ripple_adder_16bit
port(
   carry_in : in std_logic;
   x : in std_logic_vector(15 downto 0);
   y : in std_logic_vector(15 downto 0);
   output : out std_logic_vector(15 downto 0);	
   carry_out : out std_logic
);
end component;
	
--Inputs
signal x : std_logic_vector(15 downto 0);
signal y : std_logic_vector(15 downto 0);
signal carry_in : std_logic;

--Outputs
signal carry_out : std_logic;
signal output : std_logic_vector(15 downto 0);

--Clock
constant clk_period : time := 20 ns;
   
begin  

-- Instantiate the Unit Under Test (UUT)

uut: ripple_adder_16bit port map (
   x => x,
   y => y,
   carry_in => carry_in,
   carry_out => carry_out,
   output => output
);
		
stim_proc: process
begin		

   x <= "1111111111111111";
	y <= "0000000000000001";
	carry_in <= '1';
	  
   wait for clk_period;	
      
   x <= "1111111111111111";
	y <= "1111111111111111";
	carry_in <= '0';
	  
   wait for clk_period;	
      
   x <= "0000000000000000";
	y <= "0000000000000000";
	carry_in <= '1';
	  
   wait for clk_period;	
      
   x <= "0000000000000000";
	y <= "1111111111111111";
	carry_in <= '0';
	  
   wait for clk_period;	
	  
end process;
   
end;