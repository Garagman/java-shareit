package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status, Pageable pageable);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long ownerId, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.start <= :now AND b.end >= :now ORDER BY b.start DESC")
    List<Booking> findCurrentByBooker(@Param("bookerId") Long bookerId, @Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.end < :now ORDER BY b.start DESC")
    List<Booking> findPastByBooker(@Param("bookerId") Long bookerId, @Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.start > :now ORDER BY b.start DESC")
    List<Booking> findFutureByBooker(@Param("bookerId") Long bookerId, @Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.start <= :now AND b.end >= :now ORDER BY b.start DESC")
    List<Booking> findCurrentByOwner(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.end < :now ORDER BY b.start DESC")
    List<Booking> findPastByOwner(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.start > :now ORDER BY b.start DESC")
    List<Booking> findFutureByOwner(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.status = 'APPROVED' AND b.end < :now ORDER BY b.end DESC LIMIT 1")
    Optional<Booking> findLastPastBookingByItemId(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.status = 'APPROVED' AND b.start > :now ORDER BY b.start ASC LIMIT 1")
    Optional<Booking> findNextFutureBookingByItemId(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN :itemIds AND b.status = 'APPROVED' AND b.end < :now")
    List<Booking> findAllPastBookingsByItemIds(@Param("itemIds") List<Long> itemIds, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN :itemIds AND b.status = 'APPROVED' AND b.start > :now")
    List<Booking> findAllFutureBookingsByItemIds(@Param("itemIds") List<Long> itemIds, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.booker.id = :bookerId AND b.status = 'APPROVED' AND b.end < :now")
    List<Booking> findPastBookingsByItemAndBooker(@Param("itemId") Long itemId, @Param("bookerId") Long bookerId, @Param("now") LocalDateTime now);
}