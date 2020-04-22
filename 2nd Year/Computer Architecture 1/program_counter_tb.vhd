library ieee;
use ieee.std_logic_1164.all;
 
entity program_counter_tb is
end program_counter_tb;
 
architecture behavior OF program_counter_tb is 
 
-- Component Declaration for the Unit Under Test (UUT)
 
component program_counter
  port( 
		pl : in std_logic;
		pi: in std_logic;
		clk: in std_logic;
		offset : in std_logic_vector(15 downto 0);
		output : out std_logic_vector(15 downto 0)
	);
end component;
    

--Inputs
signal pi : std_logic;
signal clk : std_logic := '0';
signal pl : std_logic;
signal offset : std_logic_vector(15 downto 0);

--Outputs
signal output : std_logic_vector(15 downto 0);

--Clock
constant clk_period : time := 10 ns;

begin
  -- Instantiate the Unit Under Test (UUT)
  uut: program_counter port map (
      pi => pi,
      pl => pl,
      offset => offset,
      clk => clk,
      output => output
    );

  stim_proc: process
  begin		

    clk <= '0';
    pi <= '1';
    pl <= '0';
    offset <= "0000111100001111";
	 
    wait for clk_period;

    clk <= '1';

    wait for clk_period;

    clk <= '0';
    pi <= '0';
    pl <= '1';

    wait for clk_period;

    clk <= '1';

    wait for clk_period;
   end process;

END;