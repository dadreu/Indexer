package ru.zaxar163.indexer.command.manage;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.permission.PermissionType;

import ru.zaxar163.indexer.Utils;
import ru.zaxar163.indexer.command.Command;
import ru.zaxar163.indexer.module.SwearFilter;

public class SwearFilterCommand extends Command {
	private final SwearFilter swearFilter;

	public SwearFilterCommand(final SwearFilter swearFilter) {
		super("swearfilter", "`!swearfilter` - включает/выключает фильтр мата в канале");
		this.swearFilter = swearFilter;
	}

	@Override
	public boolean canUse(final Message message) {
		return super.canUse(message)
				&& Utils.hasAnyPerm(message, PermissionType.ADMINISTRATOR, PermissionType.MANAGE_CHANNELS);
	}

	@Override
	public void onCommand(final Message message, final String[] args) throws Exception {
		if (!swearFilter.isEnabled()) {
			message.getChannel().sendMessage("Фильтр мата не настроен.");
			message.delete();
			return;
		}
		if (args.length < 1)
			throw new IllegalArgumentException("Illegal args");
		message.getMentionedChannels().stream().forEach(e -> {
			if (swearFilter.isActive(e)) {
				swearFilter.disableFor(e);
				e.sendMessage("Фильтр мата для этого канала теперь **отключен**");
			} else {
				swearFilter.enableFor(e);
				e.sendMessage("Фильтр мата для этого канала теперь **включен**");
			}
		});
		message.delete();
	}
}
