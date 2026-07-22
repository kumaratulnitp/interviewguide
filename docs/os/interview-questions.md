# Operating Systems Interview Questions

Questions and concise answer points for computer-science graduates. Explain each answer with an example or trade-off in an interview.

## Processes and Threads

1. **What is a process? What resources does it contain?**
   - A process is a program in execution, with its own virtual address space, program counter, registers, open-file state, and OS-managed metadata in a process control block (PCB).

2. **What is the difference between a process and a thread?**
   - Processes isolate memory and resources; threads in the same process share code, heap, and open files but have separate stacks and registers. Threads are cheaper to create and switch, but require synchronization.

3. **What is a context switch?**
   - The OS saves the CPU state of one runnable task and restores another. It enables multitasking but adds overhead and can disturb CPU caches.

4. **What are user mode and kernel mode?**
   - User mode restricts privileged instructions and direct device access. Kernel mode has full hardware access. A system call is the controlled transition between them.

5. **What happens when a process calls `fork()` and `exec()`?**
   - `fork()` creates a child process, commonly using copy-on-write pages. `exec()` replaces that process's program image with a new executable.

## CPU Scheduling and Synchronization

6. **Compare FCFS, SJF/SRTF, Round Robin, and priority scheduling.**
   - FCFS is simple but can suffer the convoy effect. SJF minimizes average waiting time when burst lengths are known. Round Robin improves responsiveness using a time quantum. Priority scheduling may starve low-priority work unless aging is used.

7. **What is a race condition?**
   - It occurs when a result depends on the timing of concurrent operations on shared state, such as two threads incrementing the same unsynchronized counter.

8. **Compare mutexes, semaphores, and monitors.**
   - A mutex provides exclusive ownership. A semaphore is a counter used for mutual exclusion or resource availability. A monitor combines protected state with synchronized methods and condition waiting.

9. **What is a deadlock? State the four necessary conditions.**
   - Deadlock is permanent waiting among tasks. The conditions are mutual exclusion, hold-and-wait, no preemption, and circular wait. Prevent it by breaking one condition; avoid it with safe-state allocation; or detect and recover.

10. **What are starvation and livelock?**
    - Starvation is indefinite postponement of a task. Livelock means tasks continue changing state but make no useful progress. Both differ from deadlock, where tasks are blocked.

## Memory Management

11. **What is virtual memory?**
    - It gives each process a private logical address space and maps virtual pages to physical frames, enabling isolation and programs larger than RAM.

12. **What is paging? What is a page table and a TLB?**
    - Paging divides memory into fixed-size pages and frames. A page table stores mappings; the translation lookaside buffer (TLB) caches recent mappings to avoid frequent page-table walks.

13. **What is a page fault?**
    - It occurs when a referenced page is not resident in memory. The OS validates the access, loads the page from backing storage if valid, updates mappings, and restarts the instruction.

14. **Compare internal and external fragmentation.**
    - Internal fragmentation is unused space inside an allocated block, common with fixed-size allocation. External fragmentation is free space split into noncontiguous holes, common with variable-size allocation.

15. **What is thrashing?**
    - Excessive page faults cause the system to spend most of its time swapping rather than executing. It can be reduced by lowering multiprogramming, allocating enough working-set pages, or improving replacement policy.

## Storage, Files, and I/O

16. **What is the difference between a file descriptor and an inode?**
    - A file descriptor is a per-process handle to an open file. An inode stores file metadata and pointers to data blocks; directory entries map names to inodes.

17. **What is buffering, caching, and spooling?**
    - Buffering smooths speed differences during data transfer. Caching retains data for faster reuse. Spooling queues work for a shared device, such as a printer.

18. **What is DMA?**
    - Direct Memory Access lets a device transfer data to or from main memory with minimal CPU involvement; the CPU configures the transfer and handles completion.

19. **What is an interrupt? How does it differ from polling?**
    - An interrupt asynchronously asks the CPU to run a handler. Polling repeatedly checks device status. Interrupts avoid wasted checks at low event rates; polling can be efficient for very frequent events.

20. **What is memory-mapped I/O?**
    - Device registers or files are mapped into an address space, so ordinary memory operations access them. File mapping can simplify access and leverage page caching.

## Common Discussion Prompts

- How would you make a bounded producer-consumer queue safe for multiple threads?
- Why can a smaller Round Robin time quantum improve responsiveness but reduce throughput?
- Trace a write to a file from an application call to durable storage.
- Explain why copy-on-write makes process creation efficient.
