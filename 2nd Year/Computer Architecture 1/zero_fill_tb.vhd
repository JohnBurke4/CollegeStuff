library ieee;
use ieee.std_logic_1164.all;
 
entity zero_fill_tb is
end zero_fill_tb;
 
architecture behavior of zero_fill_tb is 
 
--Component Declaration for the Unit Under Test (UUT)
 
component zero_fill
port( 
	input: in std_logic_vector(2 downto 0);
	output : out std_logic_vector( 15 downto 0)
);	
end component;
    
--Inputs
signal input : std_logic_vector(2 downto 0);
 
--Outputs
signal output : std_logic_vector(15 downto 0);
 
--Clock
constant clk_period : time := 5 ns;

begin
 
   -- Instantiate the Unit Under Test (UUT)
   uut: zero_fill port map (
		  input => input,
		  output => output
        );

   stim_proc: process
   begin		
	
	   input <= "001";
	
      wait for clk_period;	

		input <= "100";
	  
      wait for clk_period;	
		
   end process;

end;