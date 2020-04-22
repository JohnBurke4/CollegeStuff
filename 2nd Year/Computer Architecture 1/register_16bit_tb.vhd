library ieee;
use ieee.std_logic_1164.all;

entity register_16bit_tb is
end register_16bit_tb;
 
architecture behavior of register_16bit_tb is 
 
  --Component Declaration for the Unit Under Test (UUT)
 
  component register_16bit
    port(
		    input: in std_logic_vector(15 downto 0);
		    load, clk : in std_logic;
		    output: out std_logic_vector(15 downto 0)
	  );
    end component;
    

  --Inputs
  signal load : std_logic := '0';
  signal clk : std_logic := '0';
  signal input : std_logic_vector(15 downto 0) := (others => '0');

  --Outputs
  signal output : std_logic_vector(15 downto 0);

  --Clock
  constant clk_period : time := 2 ns;
   
begin
 
	-- Instantiate the Unit Under Test (UUT)
  uut: register_16bit port map (
      load => load,
      clk => clk,
      input => input,
      output => output
    );

  stim_proc: process
  begin		

    load <= '1';
	  input <= "1111111111111111";
    clk <= '0';
    
    wait for clk_period;	

    clk <= '1';
	  
    wait for clk_period;	

		clk <= '0';
		input <= "0000000100000000";
		
    wait for clk_period;
    	
    clk <= '1';
	  
    wait for clk_period;	
    
		clk <= '0';
		load <= '0';
		input <= "0000111111110000";
		
    wait for clk_period;
    
		clk <= '1';
	  
	  wait for clk_period;
     
   end process;

END;