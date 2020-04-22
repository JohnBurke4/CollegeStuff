--------------------------------------------------------------------------------
library ieee;
use ieee.std_logic_1164.all;
 
 
entity shifter_16bit_tb is
end shifter_16bit_tb;
 
architecture behavior of shifter_16bit_tb is 
 
--Component Declaration for the Unit Under Test (UUT)
 
component shifter_16bit
  port(
      s0, s1 : in  std_logic;
      input : in  std_logic_vector (15 downto 0);
      output : out  std_logic_vector (15 downto 0)
  );
end component;
    

--Inputs
signal s0 : std_logic := '0';
signal s1 : std_logic := '0';
signal input : std_logic_vector(15 downto 0) := (others => '0');
   
--Outputs
signal output : std_logic_vector(15 downto 0);

--Clock
constant clk_period : time := 5 ns;
 
begin
 
	-- Instantiate the Unit Under Test (UUT)
  uut : shifter_16bit port map(
        s0 => s0,
		    s1 => s1,
        input => input,
		    output => output
  );

   stim_proc: process
   begin

      input <= "1111111111111111";
	    s0 <= '0';
	    s1 <= '0';
		
      wait for 5 ns;

	    s0 <= '1';
	    s1 <= '0';
	
      wait for 5 ns;	
      
	    s0 <= '0';
	    s1 <= '1';
	
      wait for 5 ns;
      	
	    s0 <= '1';
	    s1 <= '1';
      
	    wait for 5 ns;	

  end process;

end;
