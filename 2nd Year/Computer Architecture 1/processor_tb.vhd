library ieee;
use ieee.std_logic_1164.all;
 
entity processor_tb is
end processor_tb;
 
architecture behavior of processor_tb is 
 
-- Component Declaration for the Unit Under Test (UUT)
component processor
port( 
    clk : in std_logic;
    reg0: out std_logic_vector(15 downto 0);
    reg1: out std_logic_vector(15 downto 0);
    reg2: out std_logic_vector(15 downto 0);
    reg3: out std_logic_vector(15 downto 0);
    reg4: out std_logic_vector(15 downto 0);
    reg5: out std_logic_vector(15 downto 0);
    reg6: out std_logic_vector(15 downto 0);
    reg7: out std_logic_vector(15 downto 0);
    reg8: out std_logic_vector(15 downto 0);
    --car_test: out std_logic_vector(7 downto 0);
    --MS_test: out std_logic_vector(2 downto 0);
    PC_test : out std_logic_vector(15 downto 0);
    control_out : out std_logic_vector(27 downto 0);
    ir_in : out std_logic_vector(15 downto 0)
    --muxMOut : out std_logic_vector(15 downto 0);
    --opcodeOut : out std_logic_vector(7 downto 0);
    --setbit_test : out std_logic
);
end component;

--Inputs
signal clk : std_logic;

--Outputs
signal reg0 : std_logic_vector(15 downto 0);
signal reg1 : std_logic_vector(15 downto 0);
signal reg2 : std_logic_vector(15 downto 0);
signal reg3 : std_logic_vector(15 downto 0);
signal reg4 : std_logic_vector(15 downto 0);
signal reg5 : std_logic_vector(15 downto 0);
signal reg6 : std_logic_vector(15 downto 0);
signal reg7 : std_logic_vector(15 downto 0);
signal reg8 : std_logic_vector(15 downto 0);
signal control_out : std_logic_vector(27 downto 0);
signal ir_in : std_logic_vector(15 downto 0);
signal PC_test : std_logic_vector(15 downto 0);

--Clock
constant clk_period : time := 20 ns;

begin
 
    -- Instantiate the Unit Under Test (UUT)
    uut: processor port map (
        clk => clk,
        reg0 => reg0,
        reg1 => reg1,
        reg2 => reg2,
        reg3 => reg3,
        reg4 => reg4,
        reg5 => reg5,
        reg6 => reg6,
        reg7 => reg7,
        reg8 => reg8,
        control_out => control_out,
        ir_in => ir_in,
        PC_test => PC_test
    );

    stim_proc: process
    begin	

	    clk <= '0';
      
        wait for clk_period;	

		clk <= '1';
      
        wait for clk_period;	
		
   end process;

end;