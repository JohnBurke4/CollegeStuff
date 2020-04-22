library ieee;
use ieee.std_logic_1164.all;

entity multiplexor_8_16bit_tb is
end multiplexor_8_16bit_tb;
 
architecture behavior of multiplexor_8_16bit_tb is 
 
--Component Declaration for the Unit Under Test (UUT)
 
component multiplexor_8_16bit
   port(
         s0, s1, s2 : in  std_logic;
         in0 : in  std_logic_vector(15 downto 0);
         in1 : in  std_logic_vector(15 downto 0);
         in2 : in  std_logic_vector(15 downto 0);
         in3 : in  std_logic_vector(15 downto 0);
		   in4 : in  std_logic_vector(15 downto 0);
         in5 : in  std_logic_vector(15 downto 0);
         in6 : in  std_logic_vector(15 downto 0);
         in7 : in  std_logic_vector(15 downto 0);
         output : out  std_logic_vector(15 downto 0)
   );
end component;
    
--Inputs
signal s0 : std_logic := '0';
signal s1 : std_logic := '0';
signal s2 : std_logic := '0';
signal in0 : std_logic_vector(15 downto 0) := (others => '0');
signal in1 : std_logic_vector(15 downto 0) := (others => '0');
signal in2 : std_logic_vector(15 downto 0) := (others => '0');
signal in3 : std_logic_vector(15 downto 0) := (others => '0');
signal in4 : std_logic_vector(15 downto 0) := (others => '0');
signal in5 : std_logic_vector(15 downto 0) := (others => '0');
signal in6 : std_logic_vector(15 downto 0) := (others => '0');
signal in7 : std_logic_vector(15 downto 0) := (others => '0');

--Outputs
signal output : std_logic_vector(15 downto 0);

--Clock
constant clk_period : time := 10 ns;
 
begin
 
	-- Instantiate the Unit Under Test (UUT)
   uut : multiplexor_8_16bit port map(
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

      in0 <= "1010101010101010";
		in1 <= "1100110011001100";
		in2 <= "1111000011110000";
		in3 <= "1111111100000000";
		in4 <= "1010110010101110";
		in5 <= "1101010011001100";
		in6 <= "1011001011110010";
		in7 <= "1011101100000110";	
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
