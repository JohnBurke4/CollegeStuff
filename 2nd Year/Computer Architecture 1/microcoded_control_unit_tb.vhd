library ieee;
use ieee.std_logic_1164.all;

entity microcoded_control_unit_tb IS
end microcoded_control_unit_tb;
 
architecture behavior of microcoded_control_unit_tb is 
 
    -- Component Declaration for the Unit Under Test (UUT)
 
component microcoded_control_unit
    port( 
        clk : in std_logic;
        V, C, N, Z : in std_logic;
        IR_in : in std_logic_vector(15 downto 0);
        PC_out : out std_logic_vector(15 downto 0);
        immediate_out : out std_logic_vector(15 downto 0);
        FS : out std_logic_vector(4 downto 0);
        MM : out std_logic;
        MW : out std_logic;
        MB : out std_logic;
        MD : out std_logic;
        RW : out std_logic;
        TD : out std_logic;
        TA : out std_logic;
        TB : out std_logic;
        DR : out std_logic_vector(2 downto 0);
        SA : out std_logic_vector(2 downto 0);
        SB : out std_logic_vector(2 downto 0);
        control_out : out std_logic_vector(27 downto 0)
    );
end component;

--Input
signal clk : std_logic;
signal V, C, N, Z : std_logic;
signal IR_in : std_logic_vector(15 downto 0);

--Outputs
signal PC_out : std_logic_vector(15 downto 0);
signal immediate_out : std_logic_vector(15 downto 0);
signal FS : std_logic_vector(4 downto 0);
signal MM : std_logic;
signal MW : std_logic;
signal MB : std_logic;
signal MD : std_logic;
signal RW : std_logic;
signal TD : std_logic;
signal TA : std_logic;
signal TB : std_logic;
signal DR : std_logic_vector(2 downto 0);
signal SA : std_logic_vector(2 downto 0);
signal SB : std_logic_vector(2 downto 0);
signal control_out : std_logic_vector(27 downto 0);

--Clock
constant clk_period : time := 10 ns;

begin

    -- Instantiate the Unit Under Test (UUT)
    uut : microcoded_control_unit port map(
        clk => clk,
        V => V,
        C => C,
        N => N,
        Z => Z,
        IR_in => IR_in,
        PC_out => PC_out,
        immediate_out => immediate_out,
        FS => FS,
        MM => MM,
        MW => MW,
        MB => MB,
        MD => MD,
        RW => RW,
        TD => TD,
        TA => TA,
        TB => TB,
        DR => DR,
        SA => SA,
        SB => SB,
        control_out => control_out
    );

    stim_proc : process
    begin

        clk <= '0';
        IR_in <= "0000000000000000";

        wait for clk_period;

        clk <= '1';

        wait for clk_period;
        
    end process;

end;
