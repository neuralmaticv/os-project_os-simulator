package com.college.os_project.model.assembler;

import com.college.os_project.model.processor.CPU;
import com.college.os_project.model.processor.ProcessState;
import com.college.os_project.model.processor.Register;

import java.util.ArrayList;

public class Assembler {
    private static class Instruction {
        private final String name;
        private final String opcode;
        private static final ArrayList<Instruction> instructions = new ArrayList<>();

        private Instruction(String name, String code) {
            this.name = name;
            this.opcode = code;
        }
    }

    public Assembler() {
        // instructions and their operation codes
        Instruction.instructions.add(new Instruction("HLT", "0000"));
        Instruction.instructions.add(new Instruction("MOV", "0001"));
        Instruction.instructions.add(new Instruction("ADD", "0010"));
        Instruction.instructions.add(new Instruction("SUB", "0011"));
        Instruction.instructions.add(new Instruction("INC", "0100"));
        Instruction.instructions.add(new Instruction("DEC", "0101"));
        Instruction.instructions.add(new Instruction("MUL", "0110"));
        Instruction.instructions.add(new Instruction("CMP", "0111"));
        Instruction.instructions.add(new Instruction("JMP", "1000"));
        Instruction.instructions.add(new Instruction("JZ", "1001"));  // jump if zero
        Instruction.instructions.add(new Instruction("JNZ", "1010")); // jump if not zero
    }

    public void hlt() {
        CPU.changeActiveProcessState(ProcessState.DONE);
    }

    public void mov(String regAddr1, String regAddr2) {
        System.out.println("Registers: " + regAddr1 + " " + regAddr2);

        Register r1 = CPU.getRegister(regAddr1);
        Register r2 = CPU.getRegister(regAddr2);

        if (r1 != null && r2 != null) {
            r1.setValue(r2.getValue());
        }
    }

    public void add(String regAddr1, String input) {
        Register r1 = CPU.getRegister(regAddr1);

        if (r1 != null) {
            if (input.length() == 8) {
                r1.incValue(convertFromBinary(input));
            } else if (input.length() == 4) {
                Register r2 = CPU.getRegister(input);

                if (r2 != null) {
                    r1.incValue(r2.getValue());
                }
            }
        }
    }

    public void sub(String regAddr1, String input) {
        Register r1 = CPU.getRegister(regAddr1);

        if (r1 != null) {
            if (input.length() == 8) {
                r1.decValue(convertFromBinary(input));
            } else if (input.length() == 4) {
                Register r2 = CPU.getRegister(input);

                if (r2 != null) {
                    r1.decValue(r2.getValue());
                }
            }
        }
    }

    public void inc(String regAddr) {
        Register r = CPU.getRegister(regAddr);

        if (r != null) {
            r.incValue(1);
        }
    }

    public void dec(String regAddr) {
        Register r = CPU.getRegister(regAddr);

        if (r != null) {
            r.decValue(1);
        }
    }

    public void mul(String regAddr1, String input) {
        Register r1 = CPU.getRegister(regAddr1);

        if (r1 != null) {
            if (input.length() == 8) {
                r1.mulValue(convertFromBinary(input));
            } else if (input.length() == 4) {
                Register r2 = CPU.getRegister(input);

                if (r2 != null) {
                    r1.mulValue(r2.getValue());
                }
            }
        }
    }

    public void cmp(String regAddr1, String input) {
        Register r1 = CPU.getRegister(regAddr1);

        if (r1 != null) {
            if (input.length() == 8) {
                int condition = r1.getValue() == convertFromBinary(input) ? 1 : 0;
                CPU.setZFvalue(condition);
            } else if (input.length() == 4) {
                Register r2 = CPU.getRegister(input);

                if (r2 != null) {
                    int condition = r1.getValue() == r2.getValue() ? 1 : 0;
                    CPU.setZFvalue(condition);
                }
            }
        }
    }

    public void jmp(String addr) {
        int temp = convertFromBinary(addr);

        if (temp >= CPU.getActiveProcess().getInstructionsSize()) {
            CPU.getActiveProcess().setState(ProcessState.TERMINATED);
            System.out.println("Illegal address error occurred during execution of the process with PID" + CPU.getActiveProcess().getPID());
            return;
        }

        CPU.setPCvalue(temp);
    }

    public boolean jz(String addr) {
        int temp = convertFromBinary(addr);

        if (temp >= CPU.getActiveProcess().getInstructionsSize()) {
            CPU.getActiveProcess().setState(ProcessState.TERMINATED);
            System.out.println("Illegal address error occurred during execution of the process with PID" + CPU.getActiveProcess().getPID());
            return false;
        }

        if (CPU.getZFValue() == 1) {
            CPU.setPCvalue(temp);
            return true;
        }

        return false;
    }

    public boolean jnz(String addr) {
        int temp = convertFromBinary(addr);

        if (temp >= CPU.getActiveProcess().getInstructionsSize()) {
            CPU.getActiveProcess().setState(ProcessState.TERMINATED);
            System.out.println("Illegal address error occurred during execution of the process with PID" + CPU.getActiveProcess().getPID());
            return false;
        }

        if (CPU.getZFValue() == 0) {
            CPU.setPCvalue(temp);
            return true;
        }

        return false;
    }

    private int convertFromBinary(String value) {
        return Integer.parseInt(value, 2);
    }

    public String transformToMachineCode(String assemblyInstruction) {
        String machineCode = "";
        String[] input = assemblyInstruction.split("[ |,]");

        String instructionOpcode = getInstructionCode(input[0]);
        if (instructionOpcode != null) {
            machineCode += getInstructionCode(input[0]);
        }

        if (input[0].equals("HLT")) {
            return machineCode;
        } else if (input[0].equals("JMP") || input[0].equals("JZ") || input[0].equals("JNZ")) {
            machineCode += toBinary(input[1]);
            return machineCode;
        } else if (input[0].equals("INC") || input[0].equals("DEC")) {
            machineCode += CPU.getRegisterCode(input[1]);
            return machineCode;
        } else if (CPU.getRegisterCode(input[2]) != null) {
            machineCode += CPU.getRegisterCode(input[1]) + CPU.getRegisterCode(input[2]);
            return machineCode;
        } else {
            machineCode += CPU.getRegisterCode(input[1]);
            machineCode += toBinary(input[2]);
            return machineCode;
        }
    }

    private String toBinary(String input) {
        int value = Integer.parseInt(input);
        return String.format("%8s", Integer.toBinaryString(value)).replace(' ', '0');
    }

    public String getInstructionCode(String input) {
        for (Instruction i : Instruction.instructions) {
            if (i.name.equals(input)) {
                return i.opcode;
            }
        }

        return null;
    }
}
