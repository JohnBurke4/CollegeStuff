library ieee;
use ieee.std_logic_1164.all;
 
 
entity multiplexor_4_1bit_tb is
end multiplexor_4_1bit_tb;
 
architecture behavior of multiplexor_4_1bit_tb is 
 
   -- Component Declaration for the Unit Under Test (UUT)
 
   component multiplexor_4_1bit
   port( 
			s0, s1 : in  std_logic;
         in0 : in  std_logic;
         in1 : in  std_logic;
		   in2 : in  std_logic;
		   in3 : in  std_logic;
			output : out  std_logic
	);
   end component;
    

   --Inputs
   signal s0 : std_logic := '0';
   signal s1 : std_logic := '0';

   signal in0 : std_logic;
   signal in1 : std_logic;
   signal in2 : std_logic;
   signal in3 : std_logic;

   --Outputs
   signal output : std_logic;

   --Clock
   constant clk_period : time := 10 ns;

begin
 
	-- Instantiate the Unit Under Test (UUT)
   uut: multiplexor_4_1bit port map (
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
		
      s0 <= '0';
      s1 <= '0';
      in0 <= '1';
		in1 <= '0';
		in2 <= '1';
		in3 <= '0';
	 
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
