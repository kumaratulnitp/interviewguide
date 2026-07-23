# Operating Systems Interview Questions

Questions and concise answer points for computer-science graduates. Each answer gives the core idea first, then an example, trade-off, or short snippet. System-call examples use C (the classic interface); concurrency examples use Java, since the rest of this guide is Java.

## Processes and Threads

1. **What is a process? What resources does it contain?**
   - A process is a program in execution. It owns a private virtual address space (code, data, heap, stack), a program counter and registers, open-file descriptors, and OS-managed metadata kept in a process control block (PCB).
   - The PCB is what the OS saves and restores around a context switch: process state, register snapshot, memory maps, and accounting info. Two runs of the same program are two processes with separate memory, so one crashing does not corrupt the other.

2. **What is the difference between a process and a thread?**
   - Processes isolate memory and resources. Threads within one process share the code, heap, and open files but each has its own stack, registers, and program counter.
   - Threads are cheaper to create and switch and communicate through shared memory, but that sharing is exactly what forces synchronization. A crash or bad pointer in one thread can take down the whole process, whereas separate processes stay isolated at the cost of slower inter-process communication.

3. **What is a context switch?**
   - The OS saves the CPU state of the currently running task into its PCB and restores another task's state so it can run. It is what makes one CPU appear to run many tasks at once.
   - It is pure overhead - no user work happens during the switch - and it also cools CPU caches and the TLB, so the resumed task starts with more misses. This is why an extremely small scheduling quantum hurts throughput even though it improves responsiveness.

4. **What are user mode and kernel mode?**
   - User mode blocks privileged instructions and direct device access; kernel mode has full hardware access. A CPU privilege bit enforces the boundary.
   - A system call is the controlled doorway between them: user code traps into the kernel, which validates arguments and does the privileged work. This protects the machine - a buggy program cannot directly touch a disk or another process's memory, it must ask the kernel.

5. **What happens when a process calls `fork()` and `exec()`?**
   - `fork()` creates a child that is a near-copy of the parent, commonly using copy-on-write so pages are shared until one side writes. It returns twice: 0 in the child, the child's PID in the parent. `exec()` then replaces the current program image with a new executable, keeping the same PID.

   ```c
   pid_t pid = fork();
   if (pid == 0) {
       // child: replace this image with /bin/ls
       execlp("ls", "ls", "-l", NULL);
       perror("exec failed");   // only reached if exec fails
   } else {
       wait(NULL);              // parent waits for child to finish
   }
   ```

   - This "fork then exec" split is how a shell launches a command: fork a child, exec the program in it, and let the parent keep running. Copy-on-write makes the fork cheap because the two processes physically share memory until a write forces a page copy.

## CPU Scheduling and Synchronization

6. **Compare FCFS, SJF/SRTF, Round Robin, and priority scheduling.**
   - Each policy optimizes a different goal, so the right choice depends on the workload:

     | Policy | Idea | Strength | Weakness |
     | --- | --- | --- | --- |
     | FCFS | Run in arrival order | Simple, fair by arrival | Convoy effect: one long job delays many short ones |
     | SJF / SRTF | Shortest (remaining) job first | Minimizes average waiting time | Needs burst estimates; can starve long jobs |
     | Round Robin | Fixed time quantum, rotate | Responsive, good for interactive use | Too small a quantum wastes time on context switches |
     | Priority | Highest priority first | Meets importance/deadlines | Starves low priority unless aging raises it over time |

   - Convoy effect: under FCFS a CPU-heavy job at the front makes short jobs behind it wait, inflating average turnaround. Round Robin fixes responsiveness but each switch costs overhead, so the quantum is a tuning knob between latency and throughput.

7. **What is a race condition?**
   - It occurs when the result depends on the timing of concurrent operations on shared state. The classic case is two threads running `count++`, which is really load, add, store - three steps that can interleave.

   ```
   Start: count = 0
   Thread A: read 0            Thread B: read 0
   Thread A: add 1 -> 1        Thread B: add 1 -> 1
   Thread A: write 1           Thread B: write 1
   Result: count = 1   (a lost update; it should be 2)
   ```

   - The fix is to make the update atomic or to protect the shared state with a lock so only one thread is in the critical section at a time. Race conditions are timing-dependent, so they often pass in testing and fail intermittently in production.

8. **Compare mutexes, semaphores, and monitors.**
   - A mutex gives exclusive ownership - one holder at a time, and typically only the owner releases it. A semaphore is a counter: it permits up to N concurrent holders (a counting semaphore) or acts as a lock when N is 1 (a binary semaphore). A monitor bundles shared state with the lock and condition variables so the mutual exclusion is built into the object.

   ```java
   // Monitor-style: the lock is implicit in the object
   synchronized (buffer) {
       while (buffer.isFull()) {
           buffer.wait();     // release lock and sleep until notified
       }
       buffer.add(item);
       buffer.notifyAll();    // wake threads waiting on this monitor
   }
   ```

   - Rule of thumb: a mutex expresses "only one at a time," a counting semaphore expresses "at most N resources available," and a monitor is the higher-level construct (Java's `synchronized`/`wait`/`notify`) that pairs a lock with condition waiting so you rarely manage the primitives by hand.

9. **What is a deadlock? State the four necessary conditions.**
   - Deadlock is a set of tasks each waiting forever for a resource another holds. It needs all four of: mutual exclusion, hold-and-wait, no preemption, and circular wait. Breaking any one prevents it.

   ```
   Thread A: lock(X); ... lock(Y);   // holds X, wants Y
   Thread B: lock(Y); ... lock(X);   // holds Y, wants X   -> circular wait
   ```

   - The simplest practical fix is consistent lock ordering: if every thread always acquires X before Y, the circular wait cannot form. Alternatives are avoidance (banker's algorithm keeps allocation in a safe state) and detection-and-recovery (find the cycle, roll back a victim). Databases treat a deadlock victim error as retryable for the same reason.

10. **What are starvation and livelock?**
    - Starvation is a task being indefinitely postponed while others proceed, for example a low-priority job that never runs under strict priority scheduling. Livelock is when tasks keep changing state in response to each other but make no progress, like two people repeatedly stepping the same way in a hallway.
    - Both differ from deadlock, where tasks are blocked and idle. Aging (gradually raising a waiting task's priority) cures priority starvation; adding randomized backoff breaks many livelocks.

## Memory Management

11. **What is virtual memory?**
    - It gives each process a private logical address space and maps virtual pages to physical frames through the page table and MMU. Programs use virtual addresses; hardware translates them at run time.
    - This buys isolation (a process cannot name another's memory), the illusion of contiguous space, and the ability to run programs larger than RAM by keeping only active pages resident and paging the rest to disk.

12. **What is paging? What is a page table and a TLB?**
    - Paging splits the address space into fixed-size pages and physical memory into equal-size frames, so any page can map to any frame - no need for contiguous allocation. The page table stores the page-to-frame mapping; the TLB is a small fast cache of recent translations.
    - A virtual address splits into a page number and an offset: the page number indexes the page table to find the frame, and the offset is added within it. Without a TLB every access would need an extra memory read (or several, for multi-level tables) just to translate, so the TLB hit rate strongly affects performance.

13. **What is a page fault?**
    - It is a trap raised when a referenced page is not resident in memory. The OS handler decides what to do:
      1. Check whether the access is valid (within a mapped region); if not, signal a fault such as a segmentation fault.
      2. If valid, find a free frame or evict one using the replacement policy.
      3. Load the page from backing storage, update the page table and TLB.
      4. Restart the faulting instruction, which now succeeds.
    - A *minor* fault just maps an already-in-memory page (for example a shared library), while a *major* fault requires disk I/O and is far slower. Copy-on-write pages fault on the first write so the kernel can make a private copy at that moment.

14. **Compare internal and external fragmentation.**
    - Internal fragmentation is wasted space *inside* an allocated block - a 5 KB need rounded up to an 8 KB page wastes 3 KB. External fragmentation is free space broken into scattered holes, so a large request fails even though the total free bytes suffice.
    - Fixed-size allocation (paging) eliminates external fragmentation but incurs internal waste up to nearly one page per region. Variable-size allocation (segmentation, `malloc` arenas) reduces internal waste but invites external fragmentation, sometimes needing compaction.

15. **What is thrashing?**
    - Thrashing is when a system spends most of its time servicing page faults instead of doing work, because the active pages of running processes do not fit in RAM. Throughput collapses even though the CPU looks busy handling faults.
    - It is a working-set problem: each process needs its frequently used pages resident. Fixes are reducing the degree of multiprogramming (run fewer processes), allocating enough frames to cover working sets, or adding memory. A better replacement policy helps only at the margins.

## Storage, Files, and I/O

16. **What is the difference between a file descriptor and an inode?**
    - A file descriptor is a small per-process integer handle to an open-file entry (which tracks the current offset and access mode). An inode is the on-disk structure holding a file's metadata - size, permissions, timestamps - and pointers to its data blocks.
    - Directory entries map names to inode numbers, so several names (hard links) can point to one inode. Two processes can hold descriptors to the same file with independent offsets, all ultimately referring to the same inode and data blocks.

17. **What is buffering, caching, and spooling?**
    - Buffering holds data temporarily to smooth a speed or size mismatch between producer and consumer, such as reading a file in blocks. Caching keeps a copy of data likely to be reused so future access is faster, such as the page cache. Spooling queues jobs for a slow shared device so producers need not wait.
    - The distinction is intent: a buffer bridges a rate difference for one transfer, a cache trades memory for repeated-access speed and needs an eviction policy, and a spool (as for a printer) decouples submission from a device that serves one job at a time.

18. **What is DMA?**
    - Direct Memory Access lets a device transfer data to or from main memory without the CPU copying each byte. The CPU programs the transfer (source, destination, length) and is interrupted on completion.
    - Without DMA, the CPU would busy-copy every byte (programmed I/O), wasting cycles on large transfers. DMA frees the CPU to do other work during a disk or network transfer, which is essential for high throughput.

19. **What is an interrupt? How does it differ from polling?**
    - An interrupt is an asynchronous signal that makes the CPU pause and run a handler when a device needs attention. Polling is the CPU repeatedly reading a status register to check whether the device is ready.
    - Interrupts avoid wasted checks when events are infrequent, at the cost of handler and context-switch overhead per event. Polling can win when events are very frequent (the check nearly always succeeds), which is why high-speed drivers sometimes switch to polling under load.

20. **What is memory-mapped I/O?**
    - It maps device registers or file contents into a process's address space so ordinary loads and stores access them, instead of dedicated I/O instructions or `read`/`write` calls.
    - Mapping a file with `mmap` lets the program treat it as an array while the page cache handles loading and write-back, and it enables cheap sharing between processes. The trade-off is less explicit control over exactly when data is read or flushed to disk.

## Worked Discussion Prompts

**Make a bounded producer-consumer queue safe for multiple threads.**
Use one lock plus two conditions so producers wait when full and consumers wait when empty. The `while` (not `if`) re-checks the condition after waking, guarding against spurious wakeups and lost races.

```java
class BoundedBuffer<T> {
    private final Queue<T> queue = new ArrayDeque<>();
    private final int capacity;

    BoundedBuffer(int capacity) { this.capacity = capacity; }

    synchronized void put(T item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();                 // buffer full: release lock, sleep
        }
        queue.add(item);
        notifyAll();                // a consumer may now proceed
    }

    synchronized T take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();                 // buffer empty: release lock, sleep
        }
        T item = queue.remove();
        notifyAll();                // a producer may now proceed
        return item;
    }
}
```

**Why can a smaller Round Robin quantum improve responsiveness but reduce throughput?**
A smaller quantum lets each task start sooner, lowering interactive latency. But every quantum boundary triggers a context switch, and those switches are overhead plus cache/TLB cooling. Shrink the quantum too far and the CPU spends a growing fraction of time switching rather than running user code.

**Trace a write from an application call to durable storage.**
`write()` traps into the kernel, which copies the data into the page cache and marks the page dirty; the call typically returns before the disk is touched. A flusher thread (or an explicit `fsync`) later writes dirty pages to the device, often via DMA. Durability is only guaranteed after `fsync` succeeds - which is why databases fsync the log before acknowledging a commit.

**Why does copy-on-write make process creation efficient?**
`fork` would be expensive if it duplicated the whole address space. Instead parent and child share the same physical pages marked read-only; the copy of a page happens only when one side first writes it. Since a child often calls `exec` immediately (discarding that memory), most pages are never copied at all.
