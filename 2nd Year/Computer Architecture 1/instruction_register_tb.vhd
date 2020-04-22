library ieee;
use ieee.std_logic_1164.all;

entity instruction_register_tb is
end instruction_register_tb;
 
architecture behavior of instruction_register_tb is 
 
-- Component Declaration for the Unit Under Test (UUT)
 
component instruction_register
	port( 
		load : in std_logic;
		instruction: in std_logic_vector(15 downto 0);
		clk: in std_logic;
		dest_reg, src_a, src_b : out std_logic_vector(2 downto 0);
		opcode : out std_logic_vector(7 downto 0)
	);
end component;

	--Inputs
   	signal load : std_logic;
   	signal clk : std_logic := '0';
   	signal instruction : std_logic_vector( 15 downto 0);
   
 	--Outputs
   	signal opcode : std_logic_vector(7 downto 0);
	signal dest_reg : std_logic_vector(2 downto 0);
	signal src_a : std_logic_vector(2 downto 0);
	signal src_b : std_logic_vector(2 downto 0);
 
	--Clock
	constant clk_period : time := 10 ns;
 
begin
 
	-- Instantiate the Unit Under Test (UUT)
	uut: instruction_register port map (
		load => load,
		instruction => instruction,
		clk => clk,
		dest_reg => dest_reg,
		src_a => src_a,
		src_b => src_b,
		opcode => opcode
    );

   	stim_proc: process
   	begin

		load <= '1';
		instruction <= "1111111111111111";
	 	clk <= '0';

		wait for clk_period;
		
		clk <= '1';

		wait for clk_period;

		load <= '0';
		instruction <= "1010101010101010";
		clk <= '0';
		 
		wait for clk_period;
		
		clk <= '1';

		wait for clk_period;

   end process;

end;