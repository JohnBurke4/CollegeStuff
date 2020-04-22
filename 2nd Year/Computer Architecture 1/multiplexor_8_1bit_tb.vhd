library ieee;
use ieee.std_logic_1164.all;
 
entity multiplexor_8_1bit_tb is
end multiplexor_8_1bit_tb;
 
architecture behavior of multiplexor_8_1bit_tb is 
 
-- Component Declaration for the Unit Under Test (UUT)
 
component multiplexor_8_1bit
   port(
         s0, s1, s2 : in  std_logic;
         in0 : in  std_logic;
         in1 : in  std_logic;
         in2 : in  std_logic;
         in3 : in  std_logic;
		   in4 : in  std_logic;
         in5 : in  std_logic;
         in6 : in  std_logic;
         in7 : in  std_logic;
         output : out  std_logic
   );
end component;
    

--Inputs
signal s0 : std_logic := '0';
signal s1 : std_logic := '0';
signal s2 : std_logic := '0';
signal in0 : std_logic := '0';
signal in1 : std_logic := '0';
signal in2 : std_logic := '0';
signal in3 : std_logic := '0';
signal in4 : std_logic := '0';
signal in5 : std_logic := '0';
signal in6 : std_logic := '0';
signal in7 : std_logic := '0';

--Outputs
signal output : std_logic;
   
--Clock
constant clk_period : time := 10 ns;
 
begin
 
	-- Instantiate the Unit Under Test (UUT)
   uut: multiplexor_8_1bit port map(
         s0 => s0,
		   s1 => s1,
		   s2 => s2,
         in0 => in0,
         in1 => in1,
         in2 => in2,
         in3 => in3,
		   in4 => in4,
         in5 => in5,
         in6 => in6,
         in7 => in7,
         output => output
      );

   stim_proc: process
   begin	

      in0 <= '1';
		in1 <= '1';
		in2 <= '1';
		in3 <= '1';
		in4 <= '0';
		in5 <= '0';
		in6 <= '0';
		in7 <= '0';

      s0 <= '0';
	   s1 <= '0';
	   s2 <= '0';

      wait for clk_period;	

		s0 <= '1';
	   s1 <= '0';
      s2 <= '0';
      
      wait for clk_period;
      	
		s0 <= '0';
	   s1 <= '1';
	   s2 <= '0';

      wait for clk_period;	

		s0 <= '1';
	   s1 <= '1';
	   s2 <= '0';
     
      wait for clk_period;	
      
      s0 <= '0';
	   s1 <= '0';
	   s2 <= '1';

      wait for clk_period;	

		s0 <= '1';
	   s1 <= '0';
      s2 <= '1';
      
      wait for clk_period;	

		s0 <= '0';
	   s1 <= '1';
	   s2 <= '1';

      wait for clk_period;	

		s0 <= '1';
	   s1 <= '1';
      s2 <= '1';
      
      wait for clk_period;
     
   end process;

end;
