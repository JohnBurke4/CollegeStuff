library ieee;
use ieee.std_logic_1164.all;
 
 
entity decoder_8_1bit_tb is
end decoder_8_1bit_tb;
 
architecture behavior of decoder_8_1bit_tb is 
 
--Component Declaration for the Unit Under Test (UUT)
 
component decoder_8_1bit
    port(
        s0, s1, s2 : in  std_logic;
        out0: out std_logic; 
		out1: out std_logic; 
		out2: out std_logic; 
		out3: out std_logic; 
		out4: out std_logic; 
		out5: out std_logic; 
		out6: out std_logic; 
		out7: out std_logic 
	);
end component;
    

--Inputs
signal s0 : std_logic := '0';
signal s1 : std_logic := '0';
signal s2 : std_logic := '0';

--Outputs
signal out0 : std_logic;
signal out1 : std_logic;
signal out2 : std_logic;
signal out3 : std_logic;
signal out4 : std_logic;
signal out5 : std_logic;
signal out6 : std_logic;
signal out7 : std_logic;

--Clock
constant clk_period : time := 5 ns;
 
begin
 
	-- Instantiate the Unit Under Test (UUT)
	uut: decoder_8_1bit port map(
          s0 => s0,
		  s1 => s1,
		  s2 => s2,
		  out0 => out0,
		  out1 => out1,
		  out2 => out2,
		  out3 => out3,
		  out4 => out4,
		  out5 => out5,
		  out6 => out6,
		  out7 => out7
    );

   stim_proc: process
   begin	
   	
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