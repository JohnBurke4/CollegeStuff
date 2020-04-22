
--------------------------------------------------------------------------------
library ieee;
use ieee.std_logic_1164.all;
 
 
entity multiplexor_4_16bit_tb is
end multiplexor_4_16bit_tb;
 
architecture behavior of multiplexor_4_16bit_tb is 
 
   -- Component Declaration for the Unit Under Test (UUT)
 
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
    

   --Inputs
   signal s0 : std_logic := '0';
   signal s1 : std_logic := '0';

   signal in0 : std_logic_vector(15 downto 0) := (others => '0');
   signal in1 : std_logic_vector(15 downto 0) := (others => '0');
   signal in2 : std_logic_vector(15 downto 0) := (others => '0');
   signal in3 : std_logic_vector(15 downto 0) := (others => '0');

 	--Outputs
   signal output : std_logic_vector(15 downto 0);

   --Clock
   constant clk_period : time := 5 ns;
 
begin
 
	-- Instantiate the Unit Under Test (UUT)
   uut: multiplexor_4_16bit port map (
         s0 => s0,
		   s1 => s1,
         in0 => in0,
         in1 => in1,
         in2 => in2,
         in3 => in3,
         output => output
        );

   stim_proc: process
   begin		

      in0 <= "1010101010101010";
		in1 <= "1100110011001100";
		in2 <= "1111000011110000";
		in3 <= "1111111100000000";
		s0 <= '0';
	   s1 <= '0';
	 
      wait for clk_period;	

		s0 <= '1';
	   s1 <= '0';
	 
      wait for clk_period;	

		s0 <= '0';
	   s1 <= '1';
	
      wait for clk_period;	

		s0 <= '1';
	   s1 <= '1';
	 
      wait for clk_period;	
     
      s0 <= '0';
	   s1 <= '0';
	 
      wait for clk_period;	

		s0 <= '1';
	   s1 <= '0';
	 
      wait for clk_period;

		s0 <= '0';
	   s1 <= '1';
	 
      wait for clk_period;	

		s0 <= '1';
	   s1 <= '1';
	  
      wait for clk_period;

   end process;

end;
