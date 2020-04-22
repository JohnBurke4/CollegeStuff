library ieee;
use ieee.std_logic_1164.all;

entity processor is
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
end processor;

architecture behavioral of processor is

component datapath_16bit
	port(
		DR, SA, SB : in STD_LOGIC_VECTOR(2 downto 0);
		TA, TB, TD : in STD_LOGIC;
        constant_in : in STD_LOGIC_VECTOR(15 downto 0);
		W : in STD_LOGIC;
		clk : in STD_LOGIC;
        MB : in STD_LOGIC;
		MD : in STD_LOGIC;
		FS : in STD_LOGIC_VECTOR(4 downto 0);
		data_in : in STD_LOGIC_VECTOR(15 downto 0);
		N : out STD_LOGIC;
		V : out STD_LOGIC;
		C : out STD_LOGIC;
		Z : out STD_LOGIC;
		bus_a : out STD_LOGIC_VECTOR(15 downto 0);
		bus_b : out STD_LOGIC_VECTOR(15 downto 0);
		reg0 : out STD_LOGIC_VECTOR( 15 downto 0);
		reg1 : out STD_LOGIC_VECTOR( 15 downto 0);
		reg2 : out STD_LOGIC_VECTOR( 15 downto 0);
		reg3 : out STD_LOGIC_VECTOR( 15 downto 0);
		reg4 : out STD_LOGIC_VECTOR( 15 downto 0);
		reg5 : out STD_LOGIC_VECTOR( 15 downto 0);
		reg6 : out STD_LOGIC_VECTOR( 15 downto 0);
		reg7 : out STD_LOGIC_VECTOR( 15 downto 0);
		reg8 : out STD_LOGIC_VECTOR( 15 downto 0)
    );
end component;

component memory_m
  port( 
      memory_write : in std_logic;
		  clk : in std_logic;
		  data_in : in std_logic_vector(15 downto 0);
		  address : in std_logic_vector(15 downto 0);
      data_out : out std_logic_vector(15 downto 0)
  );
end component;

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

component multiplexor_2_16bit
    port(
        s : in  std_logic;
        in0 : in  std_logic_vector(15 downto 0);
        in1 : in  std_logic_vector(15 downto 0);
        output : out  std_logic_vector(15 downto 0)
    );
end component;

signal V, C, N, Z : std_logic;
signal memory_out : std_logic_vector(15 downto 0);

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

signal bus_a : std_logic_vector(15 downto 0);
signal bus_b : std_logic_vector(15 downto 0);

signal memory_address : std_logic_vector(15 downto 0);

begin 

datapath: datapath_16bit port map(
	TA => TA,
	TB => TB,
	TD => TD,
	DR=>DR,
	SA=>SA,
	SB=>SB,
	constant_in=>immediate_out,
	W=>RW,
	clk=>clk,
	MB=>MB,
	MD=>MD,
	FS=>FS,
	data_in=>memory_out,
	N=>N,
	V=>V,
	C=>C,
	Z=>Z,
	bus_a=>bus_a,
	bus_b=>bus_b,
	reg0=>reg0,
	reg1=>reg1,
	reg2=>reg2,
	reg3=>reg3,
	reg4=>reg4,
	reg5=>reg5,
	reg6=>reg6,
	reg7=>reg7,
	reg8 => reg8
);

control_unit : microcoded_control_unit port map(
        clk => clk,
        V => V,
        C => C,
        N => N,
        Z => Z,
        IR_in => memory_out,
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

mux_m: multiplexor_2_16bit port map (
    s => MM,
    in0 => bus_a,
    in1 => PC_out,
    output => memory_address
);

memory: memory_m port map (
    memory_write => MW,
	clk => clk,
	data_in => bus_b,
    data_out => memory_out,
    address => memory_address
);

ir_in <= memory_out;
PC_test <= PC_out;

end behavioral;