library ieee;
use ieee.std_logic_1164.all;
 
entity arithmetic_unit_16bit_tb is
end arithmetic_unit_16bit_tb;
 
architecture behavior of arithmetic_unit_16bit_tb is 
 
--Component Declaration for the Unit Under Test (UUT)
 
component arithmetic_unit_16bit
port(
	s : in std_logic_vector(1 downto 0); 
      carry_in : in  std_logic;
      input_a : in  std_logic_vector(15 downto 0);
      input_b : in  std_logic_vector(15 downto 0);
	output : out  std_logic_vector(15 downto 0);
	carry_out : out  std_logic
);
end component;
    

--Inputs
signal s : std_logic_vector(1 downto 0);
signal carry_in : std_logic;
signal input_a : std_logic_vector(15 downto 0);
signal input_b : std_logic_vector(15 downto 0);
   
--Outputs
signal output : std_logic_vector(15 downto 0);
signal carry_out : std_logic;

--Clock
constant clk_period : time := 20 ns;
 
begin

	-- Instantiate the Unit Under Test (UUT)
      uut: arithmetic_unit_16bit port map (
            s => s,
            carry_in => carry_in,
            input_a => input_a,
            input_b => input_b,
            carry_out => carry_out,
            output => output
      );

      stim_proc: process
      begin	

            input_a <= "1111111111111111";
	      input_b <= "0000000000000000";
	      s <= "01";
	      carry_in <= '0';
		
            wait for clk_period;	

            input_a <= "1111111111111111";
	      input_b <= "0000000000000000";
	      s <= "10";
	      carry_in <= '1';
		
            wait for clk_period;	
            
            input_a <= "1111111111111111";
	      input_b <= "0000000000000000";
	      s <= "11";
	      carry_in <= '0';
		
            wait for clk_period;	

            input_a <= "0000000000000000";
            input_b <= "0000000000000000";
            s <= "01";
            carry_in <= '1';
		
            wait for clk_period;	

            input_a <= "0000000000000000";
	      input_b <= "1111111111111111";
	      s <= "00";
	      carry_in <= '1';
		
            wait for clk_period;	
      
      end process;

end;
