library ieee;
use ieee.std_logic_1164.all;

entity microcoded_control_unit is
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
end microcoded_control_unit;

architecture behavioral of microcoded_control_unit is

component program_counter
port( 
    pl : in std_logic;
    pi: in std_logic;
    clk: in std_logic;
    offset : in std_logic_vector(15 downto 0);
    output : out std_logic_vector(15 downto 0)
);
end component;

component offset_extend
port( 
   dr : in std_logic_vector(2 downto 0);
	sb : in std_logic_vector(2 downto 0);
	output : out std_logic_vector( 15 downto 0)
);
end component; 

component function_unit_16bit
port( 
	s : in std_logic_vector(4 downto 0);
	V, C, N, Z : out  std_logic;
    input_a : in  std_logic_vector (15 downto 0);
	input_b : in  std_logic_vector (15 downto 0);
    output : out  std_logic_vector (15 downto 0)
);
end component;

component zero_fill
port( 
	input: in std_logic_vector(2 downto 0);
	output : out std_logic_vector( 15 downto 0)
);	
end component;

component multiplexor_2_8bit
port( 
    s : in  std_logic;
    in0 : in  std_logic_vector (7 downto 0);
    in1 : in  std_logic_vector (7 downto 0);
    output : out  std_logic_vector (7 downto 0)
);
end component;

component instruction_register
port( 
	load : in std_logic;
	instruction: in std_logic_vector(15 downto 0);
	clk: in std_logic;
	dest_reg, src_a, src_b : out std_logic_vector(2 downto 0);
	opcode : out std_logic_vector(7 downto 0)
);
end component;


component control_address_register
port( 
	address : in std_logic_vector(7 downto 0);
	load: in std_logic;
	clk : in std_logic;
	output : out std_logic_vector(7 downto 0)
);
end component;

component control_memory
port(
    MW : out std_logic;
    MM : out std_logic;
    RW : out std_logic;
    MD : out std_logic;
    FS : out std_logic_vector(4 downto 0);
    MB : out std_logic;
    TB : out std_logic;
    TA : out std_logic;
    TD : out std_logic;
    PL : out std_logic;
    PI : out std_logic;
    IL : out std_logic;
    MC : out std_logic;
    MS : out std_logic_vector(2 downto 0);
    NA : out std_logic_vector(7 downto 0);
    input : in std_logic_vector(7 downto 0);
    output : out std_logic_vector(27 downto 0)
);
end component;

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

signal MS : std_logic_vector(2 downto 0);
signal not_C, not_V : std_logic;
signal offset : std_logic_vector(15 downto 0);
signal PL : std_logic;
signal PI : std_logic;
signal IR_DR, IR_SB : std_logic_vector(2 downto 0);
signal IL : std_logic;
signal opcode : std_logic_vector(7 downto 0);
signal MC : std_logic;
signal NA : std_logic_vector(7 downto 0);
signal car_in : std_logic_vector(7 downto 0);
signal car_select : std_logic;
signal car_out : std_logic_vector(7 downto 0);

begin

    not_C <= not C;
    not_V <= not V;

    mux_s : multiplexor_8_1bit port map(
        s0 => MS(0),
        s1 => MS(1),
        s2 => MS(2),
        in0 => '0',
        in1 => '1',
        in2 => C,
        in3 => V,
        in4 => Z,
        in5 => N,
        in6 => not_C,
        in7 => not_V,
        output => car_select
    );

    pc : program_counter port map(
        pl => PL,
        pi => PI,
        clk => clk,
        offset => offset,
        output => PC_out 
    );

    DR <= IR_DR;
    SB <= IR_SB;

    extend : offset_extend port map(
        dr => IR_DR,
        sb => IR_SB,
        output => offset
    );

    ir : instruction_register port map(
        load => IL,
        instruction => IR_in,
        clk => clk,
        dest_reg => IR_DR,
        src_a => SA,
        src_b => IR_SB,
        opcode => opcode
    );

    fill : zero_fill port map(
        input => IR_SB,
        output => immediate_out
    );

    mux_c : multiplexor_2_8bit port map(
        s => MC,
        in0 => NA,
        in1 => opcode,
        output => car_in
    );

    car : control_address_register port map(
        address => car_in,
        load => car_select,
        clk => clk,
        output => car_out
    );

    control_m : control_memory port map(
        MW => MW,
        MM => MM,
        RW => RW,
        MD => MD,
        FS => FS,
        MB => MB,
        TB => TB,
        TA => TA,
        TD => TD,
        PL => PL,
        PI => PI,
        IL => IL,
        MC => MC,
        MS => MS,
        NA => NA,
        input => car_out,
        output => control_out
    );

end behavioral;

