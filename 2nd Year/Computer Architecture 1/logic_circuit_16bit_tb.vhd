library ieee;
use ieee.std_logic_1164.all;
 
entity logic_circuit_16bit_tb is
end logic_circuit_16bit_tb;

architecture behavioral of logic_circuit_16bit_tb is

-- Component Declaration for the Unit Under Test (UUT)

component logic_circuit_16bit
	port( 
		s : in std_logic_vector(1 downto 0);
		input_a : in  std_logic_vector(15 downto 0);
		input_b : in  std_logic_vector(15 downto 0);
		output : out  std_logic_vector(15 downto 0)
	);
end component;
	
--Inputs
signal s : std_logic_vector(1 downto 0);
signal input_a : std_logic_vector(15 downto 0);
signal input_b : std_logic_vector(15 downto 0);
   

--Outputs
signal output : std_logic_vector(15 downto 0);

--Clock
constant clk_period : time := 10ns;
   
begin   

-- Instantiate the Unit Under Test (UUT)
uut: logic_circuit_16bit port map (
          s => s,
          input_a => input_a,
		  input_b => input_b,
		  output => output
        );
		
stim_proc: process
begin

    input_a <= "0000000000000000";
	input_b <= "0000000000000001";
	s <= "00";
	  
    wait for 10 ns;	
	s <= "01";
	  
	wait for 10 ns;	
	s <= "10";
	  
	wait for 10 ns;

	s <= "11";
	  
	wait for 10 ns;	
	  
end process;
   
end;