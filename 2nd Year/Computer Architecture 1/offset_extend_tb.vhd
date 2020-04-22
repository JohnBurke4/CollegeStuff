library ieee;
use ieee.std_logic_1164.all;
 
entity offset_extend_tb is
end offset_extend_tb;
 
architecture behavior of offset_extend_tb is 
 
-- Component Declaration for the Unit Under Test (UUT)
 
component offset_extend
port( 
   dr : in std_logic_vector(2 downto 0);
	sb : in std_logic_vector(2 downto 0);
	output : out std_logic_vector( 15 downto 0)
);	
end component; 

--Inputs
signal dr : std_logic_vector(2 downto 0);
signal sb : std_logic_vector(2 downto 0);

--Outputs
signal output : std_logic_vector(15 downto 0);

--Clock
constant clk_period : time := 10 ns; 

begin
 
	-- Instantiate the Unit Under Test (UUT)
   uut: offset_extend port map (
      dr => dr,
		sb => sb,
		output => output
      );

   stim_proc: process
   begin	

	   dr<="100";
      sb<="001";
      
      wait for 5 ns;	

		dr<="000";
      sb<="001";
      
      wait for 5 ns;	
		
   end process;

end;