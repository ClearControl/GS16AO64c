# GS16AO64c

Final version of module to control 64 channel DAC card from General Standards (model 16AO64c)

This module consists of two methods:

GSBuffer:   Uses JNA and coremem to allocate memory offheap.
            Provides methods for function generation.
            Provides methods for proper data formatting specifically for the 16AO64c

GSSequencer:  Receives ArrayDeque of GSBuffers
              sends this ArrayDeque to the outputs continuously until it is empty.
